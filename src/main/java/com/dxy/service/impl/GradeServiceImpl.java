package com.dxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.ExamGradeMapper;
import com.dxy.mapper.GradeCourseMapper;
import com.dxy.mapper.GradeMapper;
import com.dxy.pojo.ExamGrade;
import com.dxy.pojo.Grade;
import com.dxy.pojo.GradeCourse;
import com.dxy.pojo.User;
import com.dxy.request.GradeUpdateRequest;
import com.dxy.response.GradesResponse;
import com.dxy.response.InsertResponse;
import com.dxy.response.UpdateResponse;
import com.dxy.service.GradeService;
import com.dxy.util.UserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements GradeService {

    @Autowired
    private GradeMapper gradeMapper;

    @Autowired
    private GradeCourseMapper gradeCourseMapper;

    @Autowired
    private ExamGradeMapper examGradeMapper;

    @Override
    public InsertResponse insert(GradeUpdateRequest grade, String token) {
        InsertResponse response = new InsertResponse();
        Grade g = new Grade();
        BeanUtils.copyProperties(grade, g);
        response.setCode(20001);
        if (UserUtil.get(token) != null && UserUtil.get(token).getType() == 0) {
            if (gradeMapper.insert(g) == 1) {
                grade.getCourses().forEach(id -> {
                    GradeCourse gradeCourse = new GradeCourse();
                    gradeCourse.setGradeId(g.getId());
                    gradeCourse.setCourseId(id);
                    gradeCourseMapper.insert(gradeCourse);
                });
                response.setCode(20000);
            }
        }
        return response;
    }

    @Override
    public UpdateResponse update(GradeUpdateRequest grade, String token) {
        UpdateResponse response = new UpdateResponse();
        Grade g = new Grade();
        BeanUtils.copyProperties(grade, g);
        response.setCode(20001);
        if (UserUtil.get(token) != null && UserUtil.get(token).getType() == 0) {
            if (gradeMapper.update(g, new LambdaUpdateWrapper<Grade>().eq(Grade::getId, grade.getId())) == 1) {
                gradeCourseMapper.delete(new LambdaQueryWrapper<GradeCourse>().eq(GradeCourse::getGradeId, g.getId()));
                grade.getCourses().forEach(id -> {
                    GradeCourse gradeCourse = new GradeCourse();
                    gradeCourse.setGradeId(g.getId());
                    gradeCourse.setCourseId(id);
                    gradeCourseMapper.insert(gradeCourse);
                });
                response.setCode(20000);
            }
        }
        return response;
    }

    @Override
    public UpdateResponse delete(Grade grade, String token) {
        UpdateResponse response = new UpdateResponse();
        response.setCode(20001);
        if (UserUtil.get(token) != null && UserUtil.get(token).getType() == 0) {
            if (gradeMapper.delete(new LambdaQueryWrapper<Grade>().eq(Grade::getId, grade.getId())) == 1) {
                response.setCode(20000);
            }
        }
        return response;
    }

    @Override
    public GradesResponse getGradesByExamId(String id, String token) {
        User user = UserUtil.get(token);
        if (user.getType() == 0) {
            ExamGrade examGrades = examGradeMapper.selectOne(new LambdaQueryWrapper<ExamGrade>().eq(ExamGrade::getExamId, id));

        }
        return null;
    }
}