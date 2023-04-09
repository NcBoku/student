package com.dxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.UserMapper;
import com.dxy.pojo.User;
import com.dxy.request.UserLoginRequest;
import com.dxy.response.UserLoginResponse;
import com.dxy.service.UserService;
import com.dxy.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    public UserLoginResponse login(String account, String password, Integer type) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        UserLoginResponse response = new UserLoginResponse();

        userQueryWrapper.eq("account", account)
                .eq("password", password)
                .eq("type", type);
        type = userMapper.selectOne(userQueryWrapper) == null ? -1 : type;
        if (type !=-1){
            HashMap<String, Object> map = new HashMap<>();
            map.put("account", account);
            map.put("type", type);
            String token = JwtUtil.generate(map);
            response.setToken(token);
        }
        response.setType(type);

        return response;
    }
}
