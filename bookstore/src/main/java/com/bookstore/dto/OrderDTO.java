package com.bookstore.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDTO {
    private Long userId;               // 用户ID
    private List<OrderItemDTO> items;  // 订单项列表

    @Data
    public static class OrderItemDTO {
        private Long bookId;           // 书籍ID
        private Integer quantity;      // 购买数量
        private BigDecimal price;      // 单价
    }
}
