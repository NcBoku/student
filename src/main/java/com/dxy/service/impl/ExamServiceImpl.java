package com.dxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.*;
import com.dxy.pojo.*;
import com.dxy.response.ExamPageResponse;
import com.dxy.response.ExamResponse;
import com.dxy.service.ExamService;
import com.dxy.util.UserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ExamService {

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private GradeMapper gradeMapper;

    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private ClazzCourseTeacherMapper clazzCourseTeacherMapper;


    @Override
    public ExamPageResponse list(Page<Exam> page, String token) {
        User user = UserUtil.get(token);
        ExamPageResponse response = new ExamPageResponse();
        response.setCode(20001);

        if (user != null) {
            Page<Exam> examPage = null;
            if (user.getType() == 2) {
                Student student = studentMapper.selectOne(new LambdaQueryWrapper<Student>().eq(Student::getUserId, user.getId()));

            } else if (user.getType() == 1) {
                Teacher teacher = teacherMapper.selectOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getUserId, user.getId()));
                List<ClazzCourseTeacher> clazzCourseTeachers = clazzCourseTeacherMapper.selectList(new LambdaQueryWrapper<ClazzCourseTeacher>()
                        .eq(ClazzCourseTeacher::getTeacherId, teacher.getId()));

            } else {
                examPage = examMapper.selectPage(page, new LambdaQueryWrapper<Exam>().orderByDesc(Exam::getTime));
            }
            response.setExams(new ArrayList<>());
            response.setCode(20000);

        }
        return response;
    }
}
