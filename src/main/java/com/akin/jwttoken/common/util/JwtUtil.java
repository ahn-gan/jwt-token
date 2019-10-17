package com.akin.jwttoken.common.util;

import com.akin.jwttoken.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
@ConfigurationProperties(prefix = "jwt", ignoreInvalidFields = true, ignoreUnknownFields = true)
public class JwtUtil {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ObjectMapper objectMapper = new ObjectMapper();

    private long expire;

    private String secret;

    public String generateToken(User user) {
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);

        try {
            return Jwts.builder()
                    .setHeaderParam("typ", "JWT")
                    .setSubject(objectMapper.writeValueAsString(user))
                    .setIssuedAt(nowDate)
                    .setExpiration(expireDate)
                    .signWith(SignatureAlgorithm.HS512, secret)
                    .compact();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            logger.debug("validate is token error ", e);
            return null;
        }
    }

    public User getUserFromClaims(Claims claims) {
        try {
            return objectMapper.readValue(claims.getSubject(), User.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

//    public static void main(String[] agrs) {
//        JwtUtil jwtUtils = new JwtUtil();
//        jwtUtils.setExpire(604800);
//        jwtUtils.setSecret("f4e2e52034348f86b67cde581c0f9eb5");
//        User user = new User();
//        user.setUserName("Akin");
//        user.setPassword("123456");
//        String token = jwtUtils.generateToken(user);
//        System.out.println(token);
//
//        User parseUser = jwtUtils.getUserFromClaims(jwtUtils.getClaimsFromToken(token));
//        System.out.println("===========" + parseUser.getUserName() + "===========" + parseUser.getPassword());
//    }
}
