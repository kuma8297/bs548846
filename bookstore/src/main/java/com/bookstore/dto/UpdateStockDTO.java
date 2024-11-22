package com.bookstore.dto;

import lombok.Data;

@Data
public class UpdateStockDTO {
    private Long bookId;       // 书籍ID
    private Integer quantity;  // 增减的数量
}
