package com.dxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dxy.pojo.Teacher;
import com.dxy.request.PageGetRequest;
import com.dxy.request.StudentUpdateRequest;
import com.dxy.request.TeacherUpdateRequest;
import com.dxy.response.InsertResponse;
import com.dxy.response.TeacherPageResponse;
import com.dxy.response.UpdateResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

public interface TeacherService extends IService<Teacher> {
    UpdateResponse update(StudentUpdateRequest request, String token);

    TeacherPageResponse list(PageGetRequest request, String token);

    InsertResponse insert(TeacherUpdateRequest teacher, String token);

    UpdateResponse delete(List<Teacher> teachers, String token);

    UpdateResponse updateAll(TeacherUpdateRequest request, @RequestHeader("X-Token") String token);
}
