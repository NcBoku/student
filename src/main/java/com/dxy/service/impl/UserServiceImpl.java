package com.dxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.UserMapper;
import com.dxy.pojo.User;
import com.dxy.response.UserInfoResponse;
import com.dxy.response.UserLoginResponse;
import com.dxy.service.UserService;
import com.dxy.util.JwtUtil;
import com.dxy.util.UserUtil;
import org.springframework.beans.BeanUtils;
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
        User user = userMapper.selectOne(userQueryWrapper);
        if (user != null ) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("account", account);
            map.put("type", type);
            String token = JwtUtil.generate(map);
            response.setToken(token);
            UserUtil.set(token, user);
            response.setCode(20000);
        }
        response.setType(type);

        return response;
    }

    @Override
    public UserInfoResponse info(String token) {
        UserInfoResponse response = new UserInfoResponse();
        User user =UserUtil.get(token);
        BeanUtils.copyProperties(user,response);
        if (user.getType()==0){
            response.setRoles("admin");
        }else if (user.getType()==1){
            response.setRoles("teacher");
        }else {
            response.setRoles("student");
        }
        response.setCode(20000);
        return response;
    }
}
