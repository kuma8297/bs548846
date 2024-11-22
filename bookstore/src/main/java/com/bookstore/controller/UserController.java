package com.bookstore.controller;

import com.bookstore.common.Result;
import com.bookstore.dto.LoginDTO;
import com.bookstore.entity.User;
import com.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result<Void> register(@RequestBody User user) {
        userService.register(user);
        return Result.success(null);
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginDTO loginDTO) {
        String token = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
        return Result.success(token);
    }


    @GetMapping("/{id}")
    public Result<User> getUserInfo(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return Result.success(user);
    }
}
