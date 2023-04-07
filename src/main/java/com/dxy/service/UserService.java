package com.dxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dxy.pojo.User;

public interface UserService extends IService<User> {
    Integer login(String account,String password,Integer type);
}
