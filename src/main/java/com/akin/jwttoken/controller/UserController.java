package com.akin.jwttoken.controller;

import com.akin.jwttoken.common.util.JwtUtil;
import com.akin.jwttoken.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody User user) {
        if (user != null) {
            String token = new JwtUtil().generateToken(user);
            return ResponseEntity.ok(token);
        }
        else
            return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity getUsers() {
        User user = new User("Akin", "Password1");
        return ResponseEntity.ok(Arrays.asList(user));
    }
}
