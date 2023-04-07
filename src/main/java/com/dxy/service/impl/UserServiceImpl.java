package com.dxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.UserMapper;
import com.dxy.pojo.User;
import com.dxy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    public Integer login(String account, String password, Integer type) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();

        userQueryWrapper.eq("account", account)
                .eq("password", password)
                .eq("type", type);

        return userMapper.selectOne(userQueryWrapper) != null ? type : -1;
    }
}
