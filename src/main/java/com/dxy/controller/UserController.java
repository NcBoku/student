package com.dxy.controller;

import com.dxy.request.UserLoginRequest;
import com.dxy.response.UserLoginResponse;
import com.dxy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public UserLoginResponse login(@RequestBody UserLoginRequest request){
        return userService.login(request.getAccount(),request.getPassword(),request.getType());
    }
}
