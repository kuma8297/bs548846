package com.bookstore.dto;

import lombok.Data;

/**
 * 查询书籍的条件
 */
@Data
public class BookQueryDTO {
    private String title;       // 书名
    private String author;      // 作者
    private Double minPrice;    // 最低价格
    private Double maxPrice;    // 最高价格
    private Integer minStock;   // 最低库存
    private Integer maxStock;   // 最高库存
}
