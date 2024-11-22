package com.bookstore.service;

import com.bookstore.entity.User;

public interface UserService {
    void register(User user);

    String login(String username, String password);

    User getUserById(Long userId);
}
