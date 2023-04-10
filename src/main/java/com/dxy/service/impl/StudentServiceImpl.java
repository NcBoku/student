package com.dxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.ClazzMapper;
import com.dxy.mapper.GradeMapper;
import com.dxy.mapper.StudentMapper;
import com.dxy.pojo.Clazz;
import com.dxy.pojo.Grade;
import com.dxy.pojo.Student;
import com.dxy.pojo.User;
import com.dxy.response.StudentResponse;
import com.dxy.service.StudentService;
import com.dxy.util.UserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private GradeMapper gradeMapper;

    @Autowired
    private ClazzMapper clazzMapper;

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
            Grade grade = gradeMapper.selectOne(new LambdaQueryWrapper<Grade>().eq(Grade::getId,student.getGradeId()));
            Clazz clazz = clazzMapper.selectOne(new LambdaQueryWrapper<Clazz>().eq(Clazz::getId,student.getClazzId()));
            BeanUtils.copyProperties(student,response);
            response.setGrade(grade.getName());
            response.setClazz(clazz.getName());
        }
        return response;
    }
}
