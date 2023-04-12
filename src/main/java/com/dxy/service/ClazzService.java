package com.dxy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dxy.pojo.Clazz;
import com.dxy.response.ClazzIdsResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface ClazzService extends IService<Clazz> {
    ClazzIdsResponse getClazzByGradeId(List<Integer> ids);
}
