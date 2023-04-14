package com.dxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.*;
import com.dxy.pojo.*;
import com.dxy.request.PageGetRequest;
import com.dxy.response.ClazzIdsResponse;
import com.dxy.response.CoursePageResponse;
import com.dxy.response.InsertResponse;
import com.dxy.response.UpdateResponse;
import com.dxy.service.CourseService;
import com.dxy.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ExamCourseMapper examCourseMapper;

    @Autowired
    private GradeCourseMapper gradeCourseMapper;

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private TeacherCourseMapper teacherCourseMapper;

    @Override
    public CoursePageResponse list(PageGetRequest request, String token) {
        Page<Course> coursePage = new Page<>(request.getPage(), request.getSize());
        User user = UserUtil.get(token);
        CoursePageResponse response = new CoursePageResponse();
        response.setCode(20001);
        response.setCourses(new ArrayList<>());
        if (user.getType() == 0) {
            LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
            if (request.getKeyword() != null) {
                wrapper.like(Course::getName, request.getKeyword())
                        .or()
                        .like(Course::getId, request.getKeyword());
            }
            Page<Course> page = courseMapper.selectPage(coursePage, wrapper.orderByDesc(Course::getId));
            response.setCode(20000);
            response.setTotal((int) page.getTotal());
            page.getRecords().forEach(e -> {
                response.getCourses().add(e);
            });
        }

        return response;
    }

    @Override
    public InsertResponse insert(Course course, String token) {
        InsertResponse response = new InsertResponse();
        response.setCode(20001);
        if (UserUtil.get(token) != null && UserUtil.get(token).getType() == 0) {
            if (courseMapper.insert(course) == 1) {
                response.setCode(20000);
            }
        }
        return response;
    }

    @Override
    public UpdateResponse update(Course course, String token) {
        UpdateResponse response = new UpdateResponse();
        response.setCode(20001);
        if (UserUtil.get(token) != null && UserUtil.get(token).getType() == 0) {
            if (courseMapper.update(course, new LambdaQueryWrapper<Course>().eq(Course::getId, course.getId())) == 1) {
                response.setCode(20000);
            }
        }
        return response;
    }

    @Override
    public UpdateResponse delete(List<Course> course, String token) {
        UpdateResponse response = new UpdateResponse();
        response.setCode(20001);
        if (UserUtil.get(token) != null && UserUtil.get(token).getType() == 0) {
            ArrayList<Integer> ids = new ArrayList<>();
            course.forEach(e -> {
                ids.add(e.getId());
            });
            if (courseMapper.delete(new LambdaQueryWrapper<Course>().in(Course::getId, ids)) == course.size()) {
                response.setCode(20000);
            }
        }
        return response;
    }

    @Override
    public CoursePageResponse getCourseByExamId(Integer id, String token) {
        CoursePageResponse response = new CoursePageResponse();
        response.setCode(20001);
        response.setCourses(new ArrayList<>());
        if (UserUtil.get(token).getType() == 0) {
            List<ExamCourse> examCourses = examCourseMapper.selectList(new LambdaQueryWrapper<ExamCourse>().eq(ExamCourse::getExamId, id));
            if (examCourses.size() != 0) {
                ArrayList<Integer> ids = new ArrayList<>();
                examCourses.forEach(e -> {
                    ids.add(e.getCourseId());
                });
                List<Course> courses = courseMapper.selectList(new LambdaQueryWrapper<Course>().in(Course::getId, ids));
                response.setCode(20000);
                response.setCourses(courses);
            }
        }
        return response;
    }
}
