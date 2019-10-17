package com.akin.jwttoken.common.interceptor;

import com.akin.jwttoken.common.util.JwtUtil;
import com.akin.jwttoken.model.User;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    private final String TOKEN_KEY = "Token";
    private final String USER_KEY = "User";

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            //如果不是HandlerMethod或者忽略登录
            logger.info("无需token校验,handler:{}", handler);
            return true;
        }

        String token = request.getHeader(TOKEN_KEY);
        if (null == token || StringUtils.isEmpty(token)) {
            logger.info("没有Token");
            return false;
        }

        Claims claims = jwtUtil.getClaimsFromToken(token);
        if (null == claims || jwtUtil.isTokenExpired(claims.getExpiration())) {
            logger.info("Token过期，请重新登录");
            return false;
        }
        // 获取用户信息
        User user = jwtUtil.getUserFromClaims(claims);
        if (null == user) {
            logger.info("token有误");
            return false;
        }
        // todo 根据用户信息进行数据库匹配
        request.setAttribute(USER_KEY, user);
        return true;
    }
}
