package com.bookstore;

import com.bookstore.entity.User;
import com.bookstore.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class BookstoreApplicationTests {

    @Resource
    private UserService userService;

    @Test
    void testRegister() {
        // 创建用户
        User user = new User();
        user.setUsername("root");
        user.setPassword("root");
        user.setEmail("root@gmail.com");

        // 调用注册逻辑
        userService.register(user);

        // 打印测试结果
        System.out.println("User registered successfully: " + user.getUsername());
    }

    @Test
    void testLogin() {
        // 模拟登录信息
        String username = "admin";
        String password = "admin";

        // 调用登录逻辑
        String token = userService.login(username, password);

        // 打印测试结果
        System.out.println("Login successful. JWT Token: " + token);
    }

}
