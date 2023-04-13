package com.dxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.*;
import com.dxy.pojo.*;
import com.dxy.request.GradeUpdateRequest;
import com.dxy.request.PageGetRequest;
import com.dxy.response.*;
import com.dxy.service.GradeService;
import com.dxy.util.UserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

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

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ExamClazzMapper examClazzMapper;

    @Autowired
    private ClazzMapper clazzMapper;

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
    public UpdateResponse delete(List<Grade> grade, String token) {
        UpdateResponse response = new UpdateResponse();
        response.setCode(20001);
        if (UserUtil.get(token) != null && UserUtil.get(token).getType() == 0) {
            ArrayList<Integer> ids = new ArrayList<>();
            grade.forEach(e -> {
                ids.add(e.getId());
            });
            if (gradeMapper.delete(new LambdaQueryWrapper<Grade>().in(Grade::getId, ids)) == grade.size()) {
                response.setCode(20000);
            }
        }
        return response;
    }

    @Override
    public GradesResponse getGradesByExamId(String id, String token) {
        User user = UserUtil.get(token);
        GradesResponse response = new GradesResponse();
        response.setCode(20001);
        if (user.getType() == 0) {
            ExamGrade examGrades = examGradeMapper.selectOne(new LambdaQueryWrapper<ExamGrade>().eq(ExamGrade::getExamId, id));
            Grade grade = null;
            if (examGrades != null) {
                grade = gradeMapper.selectOne(new LambdaQueryWrapper<Grade>().eq(Grade::getId, examGrades.getGradeId()));
            } else {
                ExamClazz examClazz = examClazzMapper.selectOne(new LambdaQueryWrapper<ExamClazz>().eq(ExamClazz::getExamId, id));
                Clazz clazz = clazzMapper.selectOne(new LambdaQueryWrapper<Clazz>().eq(Clazz::getId, examClazz));
                grade = gradeMapper.selectOne(new LambdaQueryWrapper<Grade>().eq(Grade::getId, clazz.getGradeId()));
            }

            response.setCode(20000);
            response.setGrades(new ArrayList<>());
            response.getGrades().add(grade);
        }
        return response;
    }

    @Override
    public GradePageResponse list(PageGetRequest request, String token) {
        Page<Grade> p = new Page(request.getPage(), request.getSize());
        User user = UserUtil.get(token);
        GradePageResponse response = new GradePageResponse();
        response.setCode(20001);
        if (user.getType() == 0) {
            LambdaQueryWrapper<Grade> wrapper = new LambdaQueryWrapper<>();
            if (request.getKeyword() != null && !request.getKeyword().equals("")) {
                wrapper.like(Grade::getName, request.getKeyword())
                        .or()
                        .like(Grade::getId, request.getKeyword());
            }
            Page<Grade> page = gradeMapper.selectPage(p, wrapper.orderByDesc(Grade::getId));
            response.setCode(20000);
            response.setTotalPage((int) page.getTotal());
            response.setGrade(new ArrayList<>());
            page.getRecords().forEach(e -> {
                GradeResponse gradeResponse = new GradeResponse();
                BeanUtils.copyProperties(e, gradeResponse);
                ArrayList<Object> ids = new ArrayList<>();
                List<GradeCourse> gradeCourses = gradeCourseMapper.selectList(new LambdaQueryWrapper<GradeCourse>().eq(GradeCourse::getGradeId, e.getId()));
                gradeCourses.forEach(gc -> {
                    ids.add(gc.getCourseId());
                });
                if (ids.size() != 0) {
                    List<Course> courses = courseMapper.selectList(new LambdaQueryWrapper<Course>().in(Course::getId, ids));
                    gradeResponse.setCourses(courses);
                } else {
                    gradeResponse.setCourses(new ArrayList<>());
                }

                response.getGrade().add(gradeResponse);
            });
        }
        return response;
    }

}