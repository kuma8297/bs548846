package com.bookstore;

import com.bookstore.dto.OrderDTO;
import com.bookstore.dto.OrderDTO.OrderItemDTO;
import com.bookstore.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class OrderServiceTest {

    @Resource
    private OrderService orderService;

    /**
     * 测试创建订单（单线程）
     */
    @Test
    void testCreateOrder() {
        // 构造订单数据
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(1L);

        List<OrderItemDTO> items = new ArrayList<>();
        OrderItemDTO item1 = new OrderItemDTO();
        item1.setBookId(101L);
        item1.setQuantity(2);
        item1.setPrice(new BigDecimal("59.99"));
        items.add(item1);

        OrderItemDTO item2 = new OrderItemDTO();
        item2.setBookId(102L);
        item2.setQuantity(1);
        item2.setPrice(new BigDecimal("39.99"));
        items.add(item2);

        orderDTO.setItems(items);

        // 调用订单服务创建订单
        Long orderId = orderService.createOrder(orderDTO);
        System.out.println("Order created successfully. Order ID: " + orderId);
    }

    /**
     * 测试取消订单
     */
    @Test
    void testCancelOrder() {
        Long orderId = 1L; // 假设订单ID为 1
        orderService.cancelOrder(orderId);
        System.out.println("Order canceled successfully. Order ID: " + orderId);
    }

    /**
     * 测试并发创建订单
     */
    @Test
    void testConcurrentCreateOrder() {
        // 构造订单数据
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(1L);

        List<OrderItemDTO> items = new ArrayList<>();
        OrderItemDTO item = new OrderItemDTO();
        item.setBookId(101L);
        item.setQuantity(1);
        item.setPrice(new BigDecimal("59.99"));
        items.add(item);

        orderDTO.setItems(items);

        // 模拟多线程创建订单
        Runnable task = () -> {
            try {
                Long orderId = orderService.createOrder(orderDTO);
                System.out.println("Order created successfully. Order ID: " + orderId);
            } catch (Exception e) {
                System.err.println("Order creation failed: " + e.getMessage());
            }
        };

        // 启动多线程
        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);

        thread1.start();
        thread2.start();

        // 等待线程完成
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
