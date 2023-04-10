package com.dxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dxy.pojo.Student;
import com.dxy.response.StudentResponse;

public interface StudentService extends IService<Student> {
    StudentResponse info(String token);
}
