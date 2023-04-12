package com.dxy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dxy.pojo.Clazz;
import com.dxy.request.PageGetRequest;
import com.dxy.response.ClazzIdsResponse;
import com.dxy.response.ClazzPageResponse;
import com.dxy.response.InsertResponse;
import com.dxy.response.UpdateResponse;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface ClazzService extends IService<Clazz> {
    ClazzIdsResponse getClazzByGradeId(List<Integer> ids);
    InsertResponse insert(@RequestBody Clazz clazz, @RequestHeader("X-Token") String token);
    UpdateResponse update(@RequestBody Clazz clazz, @RequestHeader("X-Token") String token);
    UpdateResponse del(@RequestBody List<Clazz> clazz, @RequestHeader("X-Token") String token);
    ClazzPageResponse list(@RequestBody PageGetRequest request, @RequestHeader("X-Token") String token);
}
