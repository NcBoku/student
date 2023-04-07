package com.dxy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dxy.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
