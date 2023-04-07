package com.dxy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dxy.pojo.Config;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface ConfigMapper extends BaseMapper<Config> {
}
