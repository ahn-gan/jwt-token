package com.akin.jwttoken.common.interceptor;

import com.akin.jwttoken.common.util.JwtUtil;
import com.akin.jwttoken.common.util.Result;
import com.akin.jwttoken.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    private final String TOKEN_KEY = "Token";
    private final String USER_KEY = "User";

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            logger.info("无需token校验,handler:{}", handler);
            return true;
        }

        String token = request.getHeader(TOKEN_KEY);
        if (null == token || StringUtils.isEmpty(token)) {
            logger.info("没有Token");
            generateFailureResponse(response, HttpStatus.FORBIDDEN);
            return false;
        }

        Claims claims = jwtUtil.getClaimsFromToken(token);
        if (null == claims || jwtUtil.isTokenExpired(claims.getExpiration())) {
            logger.info("Token过期或者无效，请重新登录");
            generateFailureResponse(response, HttpStatus.UNAUTHORIZED);
            return false;
        }
        // 获取用户信息
        User user = jwtUtil.getUserFromClaims(claims);
        if (null == user) {
            logger.info("token有误");
            return false;
        }
        // todo check user information
        request.setAttribute(USER_KEY, user);
        return true;
    }

    private void generateFailureResponse(HttpServletResponse response, HttpStatus status) {
        try {
            response.setStatus(status.value());
            response.setContentType("application/json; charset=utf-8");
            Result errorResult = new Result("AUTHORIZED ERROR", status.getReasonPhrase(), status);
            objectMapper.writeValue(response.getWriter(), errorResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
