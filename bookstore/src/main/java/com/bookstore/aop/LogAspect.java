package com.bookstore.aop;

import com.bookstore.utils.RequestContextHolderUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class LogAspect {

    private final ObjectMapper objectMapper;

    public LogAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 定义切点，拦截所有 Controller 层的请求
     */
    @Pointcut("execution(* com.bookstore.controller..*(..))")
    public void controllerPointcut() {
    }

    /**
     * 环绕通知 - 记录入参、出参、接口地址和耗时
     */
    @Around("controllerPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result;

        try {
            // 获取入参
            Object[] args = joinPoint.getArgs();
            String argsJson = objectMapper.writeValueAsString(args);

            // 记录方法名和入参
            String methodName = joinPoint.getSignature().toShortString();
            log.info("Start -> Method: {}, Args: {}", methodName, argsJson);

            // 执行目标方法
            result = joinPoint.proceed();

            // 获取出参
            String resultJson = objectMapper.writeValueAsString(result);

            // 记录出参
            log.info("End -> Method: {}, Result: {}", methodName, resultJson);
        } catch (Exception e) {
            log.error("Error in Method: {}, Message: {}", joinPoint.getSignature().toShortString(), e.getMessage());
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("Execution Time -> Method: {}, Time: {} ms", joinPoint.getSignature().toShortString(), (endTime - startTime));
        }

        return result;
    }

    /**
     * 记录访问的 URL 地址
     */
    @Before("controllerPointcut() && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void logUrlAccess(JoinPoint joinPoint) {
        HttpServletRequest request = RequestContextHolderUtils.getHttpServletRequest();
        if (request != null) {
            log.info("Accessed URL: {}", request.getRequestURI());
        }
    }
}
