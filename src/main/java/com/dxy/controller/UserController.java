package com.dxy.controller;

import com.dxy.request.UserLoginRequest;
import com.dxy.request.UserPasswordUpdateRequest;
import com.dxy.response.UpdateResponse;
import com.dxy.response.UserInfoResponse;
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

    @GetMapping("info")
    public UserInfoResponse info(@RequestParam("token")String token){
        return userService.info(token);
    }

    @PostMapping("logout")
    public UpdateResponse logout(@RequestHeader("X-Token")String token){
        return userService.logout(token);
    }

    @PostMapping("/update/password")
    public UpdateResponse updatePassword(@RequestBody UserPasswordUpdateRequest request,@RequestHeader("X-Token")String token){
        return userService.updatePassword(request,token);
    }
}
