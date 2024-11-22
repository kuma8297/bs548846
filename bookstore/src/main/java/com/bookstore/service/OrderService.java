package com.bookstore.service;


import com.bookstore.dto.OrderDTO;

public interface OrderService {
    Long createOrder(OrderDTO orderDTO);

    void cancelOrder(Long orderId);
}
