package com.bookstore.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("order_items")
public class OrderItem {
    @TableId
    private Long id;                // 订单项ID
    private Long orderId;           // 订单ID
    private Long bookId;            // 书籍ID
    private Integer quantity;       // 数量
    private BigDecimal price;       // 单价
}
