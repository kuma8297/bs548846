package com.bookstore.service;

import com.bookstore.dto.OrderDTO;
import com.bookstore.entity.Order;
import com.bookstore.entity.OrderItem;
import com.bookstore.mapper.OrderItemMapper;
import com.bookstore.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private BookService bookService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String LOCK_PREFIX = "lock:order:";

    @Override
    @Transactional
    public Long createOrder(OrderDTO orderDTO) {
        String lockKey = LOCK_PREFIX + orderDTO.getUserId();

        try {
            Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, "LOCKED", 10, TimeUnit.SECONDS);
            if (Boolean.FALSE.equals(locked)) {
                throw new RuntimeException("Order creation is locked by another process.");
            }

            BigDecimal totalPrice = BigDecimal.ZERO;
            List<OrderItem> items = convertToOrderItems(orderDTO.getItems());

            for (OrderItem item : items) {
                bookService.updateStock(item.getBookId(), item.getQuantity());
                totalPrice = totalPrice.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }

            Order order = new Order();
            order.setUserId(orderDTO.getUserId());
            order.setTotalPrice(totalPrice);
            order.setStatus("PENDING");
            orderMapper.insert(order);

            for (OrderItem item : items) {
                item.setOrderId(order.getId());
                orderItemMapper.insert(item);
            }

            return order.getId();
        } finally {
            redisTemplate.delete(lockKey);
        }
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        String lockKey = "lock:order:" + orderId;

        try {
            // 获取分布式锁
            Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, "LOCKED", 10, TimeUnit.SECONDS);
            if (Boolean.FALSE.equals(locked)) {
                throw new RuntimeException("Cancel operation is locked by another process.");
            }

            // 查询订单
            Order order = orderMapper.selectById(orderId);
            if (order == null) {
                throw new RuntimeException("Order not found");
            }
            if ("CANCELED".equals(order.getStatus())) {
                throw new RuntimeException("Order is already canceled");
            }

            // 更新订单状态
            order.setStatus("CANCELED");
            int rows = orderMapper.updateById(order);
            if (rows == 0) {
                throw new RuntimeException("Failed to cancel order due to concurrent modification.");
            }

            // 回滚库存
            List<OrderItem> items = orderItemMapper.selectByOrderId(orderId);
            for (OrderItem item : items) {
                bookService.updateStock(item.getBookId(), -item.getQuantity());
            }
        } finally {
            // 释放分布式锁
            redisTemplate.delete(lockKey);
        }
    }

    private List<OrderItem> convertToOrderItems(List<OrderDTO.OrderItemDTO> orderItemDTOs) {
        return orderItemDTOs.stream().map(dto -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setBookId(dto.getBookId());
            orderItem.setQuantity(dto.getQuantity());
            orderItem.setPrice(dto.getPrice());
            return orderItem;
        }).collect(Collectors.toList());
    }

}
