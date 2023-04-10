package com.dxy.interceptor;

import com.dxy.util.JwtUtil;
import com.dxy.util.UserUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("X-Token");
        return request.getMethod().equals("OPTIONS") || token != null && JwtUtil.verify(token) && !JwtUtil.isExpired(token);
    }
}
