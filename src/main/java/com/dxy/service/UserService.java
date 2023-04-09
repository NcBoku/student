package com.dxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dxy.pojo.User;
import com.dxy.request.UserLoginRequest;
import com.dxy.response.UserLoginResponse;

public interface UserService extends IService<User> {
    UserLoginResponse login(String account, String password, Integer type);
}
