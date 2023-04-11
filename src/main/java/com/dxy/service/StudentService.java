package com.dxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dxy.pojo.Student;
import com.dxy.request.StudentUpdateRequest;
import com.dxy.response.StudentResponse;
import com.dxy.response.UpdateResponse;

public interface StudentService extends IService<Student> {
    StudentResponse info(String token);
    UpdateResponse update(StudentUpdateRequest studentUpdateRequest, String token);
}
