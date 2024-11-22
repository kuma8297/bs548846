package com.bookstore.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("users")
public class User {
    @TableId
    private Long id;                // 用户ID
    private String username;        // 用户名
    private String password;        // 密码
    private String email;           // 邮箱
    private LocalDateTime createdAt; // 创建时间
}
