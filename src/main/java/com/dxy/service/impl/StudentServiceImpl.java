package com.dxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.ClazzMapper;
import com.dxy.mapper.GradeMapper;
import com.dxy.mapper.StudentMapper;
import com.dxy.mapper.UserMapper;
import com.dxy.pojo.*;
import com.dxy.request.PageGetRequest;
import com.dxy.request.StudentUpdateRequest;
import com.dxy.response.StudentPageResponse;
import com.dxy.response.StudentResponse;
import com.dxy.response.UpdateResponse;
import com.dxy.service.StudentService;
import com.dxy.util.UserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private GradeMapper gradeMapper;

    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private UserMapper userMapper;


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
            Grade grade = gradeMapper.selectOne(new LambdaQueryWrapper<Grade>().eq(Grade::getId, student.getGradeId()));
            Clazz clazz = clazzMapper.selectOne(new LambdaQueryWrapper<Clazz>().eq(Clazz::getId, student.getClazzId()));
            BeanUtils.copyProperties(student, response);
            response.setGrade(grade.getName());
            response.setClazzName(clazz.getName());
        }
        return response;
    }

    @Override
    public UpdateResponse update(StudentUpdateRequest request, String token) {
        UpdateResponse response = new UpdateResponse();
        response.setCode(20000);
        User user = UserUtil.get(token);
        Student student = studentMapper.selectOne(new LambdaQueryWrapper<Student>().eq(Student::getUserId, user.getId()));
        BeanUtils.copyProperties(request, student);
        studentMapper.update(student, new LambdaQueryWrapper<Student>().eq(Student::getUserId, user.getId()));
        return response;
    }

    @Override
    public StudentPageResponse list(PageGetRequest request, String token) {
        Page<Student> studentPage = new Page<>(request.getPage(), request.getSize());
        StudentPageResponse response = new StudentPageResponse();
        response.setStudents(new ArrayList<>());
        response.setCode(20001);
        if (UserUtil.get(token).getType() == 0) {
            LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
            if (request.getKeyword() != null && !request.getKeyword().equals("")) {
                List<Clazz> clazz = clazzMapper.selectList(new LambdaQueryWrapper<Clazz>().like(Clazz::getName, request.getKeyword()));
                ArrayList<Integer> cids = new ArrayList<>();
                cids.add(-1);
                if (clazz.size() != 0) {
                    clazz.forEach(e -> {
                        cids.add(e.getId());
                    });
                }
                wrapper.like(Student::getId, request.getKeyword())
                        .or()
                        .like(Student::getNumber, request.getKeyword())
                        .or()
                        .like(Student::getSex, request.getKeyword())
                        .or()
                        .like(Student::getName, request.getKeyword())
                        .or()
                        .like(Student::getQq, request.getKeyword())
                        .or()
                        .in(Student::getClazzId, cids);
            }
            Page<Student> page = studentMapper.selectPage(studentPage, wrapper.orderByDesc(Student::getId));
            response.setCode(20000);
            response.setTotal((int) page.getTotal());
            page.getRecords().forEach(
                    e -> {
                        StudentResponse studentResponse = new StudentResponse();
                        BeanUtils.copyProperties(e, studentResponse);
                        studentResponse.setClazzName(clazzMapper.selectOne(new LambdaQueryWrapper<Clazz>().eq(Clazz::getId, e.getClazzId())).getName());
                        response.getStudents().add(studentResponse);
                    }
            );
        }
        return response;
    }

    @Override
    public UpdateResponse insert(Student student, String token) {
        UpdateResponse response = new UpdateResponse();
        response.setCode(20001);
        if (UserUtil.get(token).getType() == 0) {
            User user = new User();
            user.setType(2);
            user.setName(student.getName());
            user.setAccount(student.getNumber());
            user.setPassword(student.getNumber());
            userMapper.insert(user);
            student.setUserId(user.getId());
            if (studentMapper.insert(student) == 1) {
                response.setCode(20000);
            }
        }
        return response;
    }

    @Override
    public UpdateResponse delete(List<Student> student, String token) {
        UpdateResponse response = new UpdateResponse();
        response.setCode(20001);
        if (UserUtil.get(token).getType() == 0) {
            ArrayList<Integer> ids = new ArrayList<>();
            student.forEach(e -> {
                ids.add(e.getId());
            });
            if (studentMapper.delete(new LambdaQueryWrapper<Student>().in(Student::getId, ids)) == student.size()) {
                response.setCode(20000);
            }
        }
        return response;
    }

    @Override
    public UpdateResponse updateAll(Student student, String token) {
        UpdateResponse response = new UpdateResponse();
        response.setCode(20001);
        if (UserUtil.get(token).getType() == 0) {
            if (studentMapper.update(student, new LambdaQueryWrapper<Student>().eq(Student::getId, student.getId())) == 1) {
                response.setCode(20000);
            }
        }
        return response;
    }


}
