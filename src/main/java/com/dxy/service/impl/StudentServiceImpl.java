package com.dxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.StudentMapper;
import com.dxy.pojo.Student;
import com.dxy.pojo.User;
import com.dxy.response.StudentResponse;
import com.dxy.service.StudentService;
import com.dxy.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public StudentResponse info(String token) {
        User user = UserUtil.get(token);
        StudentResponse response = new StudentResponse();
        response.setCode(20001);
        if (user != null) {
            response.setCode(20000);
            LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Student::getUserId, user.getId());
            Student student = studentMapper.selectOne(wrapper);
            response.setStudent(student);
        }
        return response;
    }
}
