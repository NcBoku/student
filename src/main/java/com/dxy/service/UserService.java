package com.dxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dxy.pojo.User;
import com.dxy.request.UserLoginRequest;
import com.dxy.response.UpdateResponse;
import com.dxy.response.UserInfoResponse;
import com.dxy.response.UserLoginResponse;

public interface UserService extends IService<User> {
    UserLoginResponse login(String account, String password, Integer type);
    UserInfoResponse info(String token);
    UpdateResponse update(User user);
    UpdateResponse logout(String token);
}
