package com.bookstore.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bookstore.entity.User;
import com.bookstore.exception.BookstoreException;
import com.bookstore.exception.ErrorCode;
import com.bookstore.mapper.UserMapper;
import com.bookstore.service.UserService;
import com.bookstore.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void register(User user) {
        // 检查用户名是否已存在
        User existingUser = userMapper.selectOne(new QueryWrapper<User>().eq("username", user.getUsername()));
        if (existingUser != null) {
            throw new BookstoreException(ErrorCode.INVALID_CREDENTIALS.getCode(), "Username already exists");
        }

        // 加密密码并保存
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userMapper.insert(user);
    }

    @Override
    public String login(String username, String password) {
        // 查询用户
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if (user == null) {
            throw new BookstoreException(ErrorCode.USER_NOT_FOUND.getCode(), ErrorCode.USER_NOT_FOUND.getMessage());
        }

        // 校验密码
        if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            throw new BookstoreException(ErrorCode.INVALID_CREDENTIALS.getCode(), ErrorCode.INVALID_CREDENTIALS.getMessage());
        }

        // 生成 JWT Token
        return jwtUtils.generateToken(user.getUsername());
    }

    @Override
    public User getUserById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("User not found for ID: " + userId);
        }
        return user;
    }
}
