package com.bookstore.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("books")
public class Book {
    @TableId
    private Long id;                // 书籍ID
    private String title;           // 书名
    private String author;          // 作者
    private BigDecimal price;       // 价格
    private Integer stock;          // 库存
    @Version
    private Integer version;        // 乐观锁版本
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt; // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt; // 更新时间
}
