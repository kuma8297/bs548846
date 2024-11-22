package com.bookstore.controller;

import com.bookstore.common.Result;
import com.bookstore.dto.OrderDTO;
import com.bookstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Result<Long> createOrder(@RequestBody OrderDTO orderDTO) {
        Long orderId = orderService.createOrder(orderDTO);
        return Result.success(orderId);
    }

    @PutMapping("/{id}/cancel")
    public Result<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return Result.success(null);
    }
}
