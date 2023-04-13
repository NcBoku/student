package com.dxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dxy.pojo.Student;
import com.dxy.request.PageGetRequest;
import com.dxy.request.StudentUpdateRequest;
import com.dxy.response.StudentPageResponse;
import com.dxy.response.StudentResponse;
import com.dxy.response.UpdateResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

public interface StudentService extends IService<Student> {
    StudentResponse info(String token);
    UpdateResponse update(StudentUpdateRequest studentUpdateRequest, String token);
    StudentPageResponse list(@RequestBody PageGetRequest request, @RequestHeader("X-Token") String token);
    UpdateResponse insert(@RequestBody Student student,@RequestHeader("X-Token") String token);
    UpdateResponse delete(@RequestBody List<Student> student,@RequestHeader("X-Token") String token);
}
