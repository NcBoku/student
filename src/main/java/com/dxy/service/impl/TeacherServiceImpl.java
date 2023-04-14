package com.dxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.CourseMapper;
import com.dxy.mapper.TeacherCourseMapper;
import com.dxy.mapper.TeacherMapper;
import com.dxy.mapper.UserMapper;
import com.dxy.pojo.*;
import com.dxy.request.PageGetRequest;
import com.dxy.request.StudentUpdateRequest;
import com.dxy.request.TeacherUpdateRequest;
import com.dxy.response.*;
import com.dxy.service.TeacherService;
import com.dxy.util.UserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private TeacherCourseMapper teacherCourseMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UpdateResponse update(StudentUpdateRequest request, String token) {
        UpdateResponse response = new UpdateResponse();
        response.setCode(20000);
        User user = UserUtil.get(token);
        Teacher teacher = teacherMapper.selectOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getUserId, user.getId()));
        BeanUtils.copyProperties(request, teacher);
        teacherMapper.update(teacher, new LambdaQueryWrapper<Teacher>().eq(Teacher::getUserId, user.getId()));
        return response;
    }

    @Override
    public TeacherPageResponse list(PageGetRequest request, String token) {
        Page<Teacher> teacherPage = new Page<>(request.getPage(), request.getSize());
        TeacherPageResponse response = new TeacherPageResponse();
        response.setTeachers(new ArrayList<>());
        response.setCode(20001);
        if (UserUtil.get(token).getType() == 0) {
            LambdaQueryWrapper<Teacher> wrapper = new LambdaQueryWrapper<>();
            if (request.getKeyword() != null && !request.getKeyword().equals("")) {
                wrapper.like(Teacher::getId, request.getKeyword())
                        .or()
                        .like(Teacher::getNumber, request.getKeyword())
                        .or()
                        .like(Teacher::getSex, request.getKeyword())
                        .or()
                        .like(Teacher::getName, request.getKeyword())
                        .or()
                        .like(Teacher::getQq, request.getKeyword());

            }
            Page<Teacher> page = teacherMapper.selectPage(teacherPage, wrapper.orderByDesc(Teacher::getId));
            response.setCode(20000);
            response.setTotal((int) page.getTotal());
            page.getRecords().forEach(
                    e -> {
                        TeacherResponse teacherResponse = new TeacherResponse();
                        teacherResponse.setCourses(new ArrayList<>());
                        BeanUtils.copyProperties(e, teacherResponse);
                        List<TeacherCourse> teacherCourses = teacherCourseMapper.selectList(new LambdaQueryWrapper<TeacherCourse>().eq(TeacherCourse::getTeacherId, e.getId()));
                        if (teacherCourses.size() != 0) {
                            ArrayList<Integer> ids = new ArrayList<>();
                            teacherCourses.forEach(tc -> {
                                ids.add(tc.getCourseId());
                            });
                            List<Course> courses = courseMapper.selectList(new LambdaQueryWrapper<Course>().in(Course::getId, ids));
                            teacherResponse.setCourses(courses);
                        }
                        response.getTeachers().add(teacherResponse);
                    }
            );
        }
        return response;
    }

    @Override
    public InsertResponse insert(TeacherUpdateRequest request, String token) {
        InsertResponse response = new InsertResponse();
        response.setCode(20001);
        if (UserUtil.get(token).getType() == 0) {
            Teacher teacher = new Teacher();
            BeanUtils.copyProperties(request, teacher);
            User user = new User();
            user.setType(1);
            user.setName(teacher.getName());
            user.setAccount(teacher.getNumber());
            user.setPassword(teacher.getNumber());
            userMapper.insert(user);
            teacher.setUserId(user.getId());
            if (teacherMapper.insert(teacher) == 1) {

                request.getCourseIds().forEach(e -> {
                    TeacherCourse teacherCourse = new TeacherCourse();
                    teacherCourse.setTeacherId(teacher.getId());
                    teacherCourse.setCourseId(e);
                    teacherCourseMapper.insert(teacherCourse);
                });
                response.setCode(20000);
            }
        }
        return response;
    }

    @Override
    public UpdateResponse delete(List<Teacher> teachers, String token) {
        UpdateResponse response = new UpdateResponse();
        response.setCode(20001);
        if (UserUtil.get(token).getType() == 0) {
            ArrayList<Integer> ids = new ArrayList<>();
            teachers.forEach(e -> {
                ids.add(e.getId());
            });
            if (teacherMapper.delete(new LambdaQueryWrapper<Teacher>().in(Teacher::getId, ids)) == teachers.size()) {
                response.setCode(20000);
            }
        }
        return response;
    }

    @Override
    public UpdateResponse updateAll(TeacherUpdateRequest request, String token) {
        UpdateResponse response = new UpdateResponse();
        response.setCode(20001);
        if (UserUtil.get(token).getType() == 0) {
            Teacher teacher = new Teacher();
            BeanUtils.copyProperties(request, teacher);
            if (teacherMapper.update(teacher, new LambdaQueryWrapper<Teacher>().in(Teacher::getId, teacher.getId())) == 1) {
                teacherCourseMapper.delete(new LambdaQueryWrapper<TeacherCourse>().eq(TeacherCourse::getTeacherId, teacher.getId()));
                request.getCourseIds().forEach(e -> {
                    TeacherCourse teacherCourse = new TeacherCourse();
                    teacherCourse.setTeacherId(request.getId());
                    teacherCourse.setCourseId(e);
                    teacherCourseMapper.insert(teacherCourse);
                });

                response.setCode(20000);
            }
        }
        return response;
    }

    @Override
    public TeacherResponse info(String token) {
        TeacherResponse response = new TeacherResponse();
        response.setCode(20001);
        User user = UserUtil.get(token);
        if (user != null) {
            Teacher teacher = teacherMapper.selectOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getUserId, user.getId()));
            BeanUtils.copyProperties(teacher,response);
            List<TeacherCourse> teacherCourses = teacherCourseMapper.selectList(new LambdaQueryWrapper<TeacherCourse>().eq(TeacherCourse::getTeacherId, teacher.getId()));
            if (teacherCourses.size() != 0) {
                ArrayList<Integer> ids = new ArrayList<>();
                teacherCourses.forEach(e -> {
                    ids.add(e.getCourseId());
                });
                List<Course> courses = courseMapper.selectList(new LambdaQueryWrapper<Course>().in(Course::getId, ids));
                response.setCourses(courses);
            }
            response.setCode(20000);
        }
        return response;
    }
}
