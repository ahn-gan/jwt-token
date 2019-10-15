package com.akin.jwttoken.controller;

import com.akin.jwttoken.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody User user) {
        if (user != null)
            return ResponseEntity.ok(user);
        else
            return ResponseEntity.ok(HttpStatus.OK);
    }
}
