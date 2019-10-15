package com.akin.jwttoken.common.bean;

import io.jsonwebtoken.Claims;

public class JwtToken {
    private String rawToken;

    private Claims claims;

    public JwtToken() {

    }

    public JwtToken(String rawToken, Claims claims) {
        this.rawToken = rawToken;
        this.claims = claims;
    }

    public String getRawToken() {
        return rawToken;
    }

    public void setRawToken(String rawToken) {
        this.rawToken = rawToken;
    }

    public Claims getClaims() {
        return claims;
    }

    public void setClaims(Claims claims) {
        this.claims = claims;
    }
}
