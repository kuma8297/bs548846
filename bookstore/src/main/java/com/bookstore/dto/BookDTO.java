package com.bookstore.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookDTO {
    private Long id;           // 书籍ID
    private String title;      // 书名
    private String author;     // 作者
    private BigDecimal price;  // 价格
    private Integer stock;     // 库存数量
}
