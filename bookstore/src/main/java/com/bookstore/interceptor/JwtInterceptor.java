package com.bookstore.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.bookstore.common.Result;
import com.bookstore.utils.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;

    public JwtInterceptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization header is missing or invalid");
        }

        token = token.substring(7); // 去掉 Bearer 前缀
        if (jwtUtils.isTokenExpired(token)) {
            throw new RuntimeException("Token has expired");
        }

        String username = jwtUtils.extractUsername(token);
        request.setAttribute("username", username);
        return true;
    }

}
