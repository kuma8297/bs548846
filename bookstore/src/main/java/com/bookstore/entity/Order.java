package com.bookstore.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Order {
    @TableId
    private Long id;                // 订单ID
    private Long userId;            // 用户ID
    private BigDecimal totalPrice;  // 总价格
    private String status;          // 订单状态
    private LocalDateTime createdAt; // 创建时间
}
