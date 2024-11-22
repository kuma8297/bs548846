package com.bookstore.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private Long bookId;       // 书籍ID
    private Integer quantity;  // 购买数量
    private BigDecimal price;  // 单价
}
