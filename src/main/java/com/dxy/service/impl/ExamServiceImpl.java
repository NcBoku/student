package com.dxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.*;
import com.dxy.pojo.*;
import com.dxy.request.ExamInsertRequest;
import com.dxy.request.ExamScoreRequest;
import com.dxy.response.*;
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
    private ExamClazzMapper examClazzMapper;

    @Autowired
    private ExamGradeMapper examGradeMapper;

    @Autowired
    private ExamCourseMapper examCourseMapper;

    @Autowired
    private TeacherCourseMapper teacherCourseMapper;

    @Autowired
    private GradeMapper gradeMapper;

    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ScoreMapper scoreMapper;


    @Override
    public ExamPageResponse list(Page<Exam> page, String token,String keyword) {
        User user = UserUtil.get(token);
        ExamPageResponse response = new ExamPageResponse();
        response.setCode(20001);

        if (user != null) {
            ArrayList<Integer> examIds = new ArrayList<>();
            ArrayList<Integer> courseIds = new ArrayList<>();
            response.setCode(20000);
            response.setTotalPage(0);
            Page<Exam> examPage = null;
            if (user.getType() == 2) {
                Student student = studentMapper.selectOne(new LambdaQueryWrapper<Student>().eq(Student::getUserId, user.getId()));
                List<ExamGrade> examGrades = examGradeMapper.selectList(new LambdaQueryWrapper<ExamGrade>().eq(ExamGrade::getGradeId, student.getGradeId()));
                examGrades.forEach(e -> {
                    examIds.add(e.getExamId());
                });
                List<ExamClazz> examClazz = examClazzMapper.selectList(new LambdaQueryWrapper<ExamClazz>().eq(ExamClazz::getClazzId, student.getClazzId()));
                examClazz.forEach(e -> {
                    examIds.add(e.getExamId());
                });
                if (examIds.size() == 0) {
                    return response;
                }
                LambdaQueryWrapper<Exam> wrapper = new LambdaQueryWrapper<>();
                if (keyword!=null&&!keyword.equals("")){
                    wrapper.and(
                            o->o.like(Exam::getId,keyword)
                                    .or()
                                    .like(Exam::getName,keyword)
                                    .or()
                                    .like(Exam::getRemark,keyword)
                    );

                }
                examPage = examMapper.selectPage(page,wrapper.in(Exam::getId, examIds));
            } else if (user.getType() == 1) {
                Teacher teacher = teacherMapper.selectOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getUserId, user.getId()));
                List<TeacherCourse> teacherCourses = teacherCourseMapper.selectList(new LambdaQueryWrapper<TeacherCourse>().eq(TeacherCourse::getTeacherId, teacher.getId()));
                teacherCourses.forEach(e -> {
                    courseIds.add(e.getCourseId());
                });
                response.setExams(new ArrayList<>());
                if (courseIds.size() == 0) {
                    return response;
                }
                List<ExamCourse> examCourses = examCourseMapper.selectList(new LambdaQueryWrapper<ExamCourse>().in(ExamCourse::getCourseId, courseIds));
                examCourses.forEach(
                        e -> {
                            examIds.add(e.getExamId());
                        }
                );
                if (examIds.size() == 0) {
                    return response;
                }
                LambdaQueryWrapper<Exam> wrapper = new LambdaQueryWrapper<>();
                if (keyword!=null&&!keyword.equals("")){
                    wrapper.and(
                            o->o.like(Exam::getId,keyword)
                                    .or()
                                    .like(Exam::getName,keyword)
                                    .or()
                                    .like(Exam::getRemark,keyword)
                    );

                }
                examPage = examMapper.selectPage(page, wrapper.in(Exam::getId, examIds));
            } else {
                LambdaQueryWrapper<Exam> wrapper = new LambdaQueryWrapper<>();
                if (keyword!=null&&!keyword.equals("")){
                    wrapper.and(
                            o->o.like(Exam::getId,keyword)
                                    .or()
                                    .like(Exam::getName,keyword)
                                    .or()
                                    .like(Exam::getRemark,keyword)
                    );

                }
                examPage = examMapper.selectPage(page, wrapper.orderByDesc(Exam::getTime));
            }


            response.setExams(new ArrayList<>());
            response.setTotalPage((int) examPage.getTotal());
            examPage.getRecords().forEach(
                    e -> {
                        ExamResponse examResponse = new ExamResponse();
                        response.getExams().add(examResponse);
                        BeanUtils.copyProperties(e, examResponse);
                        if (e.getType() == 0) {
                            ExamGrade examGrade = examGradeMapper.selectOne(new LambdaQueryWrapper<ExamGrade>().eq(ExamGrade::getExamId, e.getId()));
                            if (examGrade == null) {
                                return;
                            }
                            Grade grade = gradeMapper.selectOne(new LambdaQueryWrapper<Grade>().eq(Grade::getId, examGrade.getGradeId()));
                            examResponse.setGradeName(grade.getName());
                            examResponse.setClazzName("");
                        } else {
                            List<ExamClazz> clazzList = examClazzMapper.selectList(new LambdaQueryWrapper<ExamClazz>().eq(ExamClazz::getExamId, e.getId()));
                            ArrayList<Integer> clazzIds = new ArrayList<>();
                            clazzList.forEach(ee -> {
                                clazzIds.add(ee.getClazzId());
                            });
                            StringBuilder clazzStr = new StringBuilder("");
                            examResponse.setGradeName("");
                            if (clazzIds.size() == 0) {
                                return;
                            }
                            clazzMapper.selectList(new LambdaQueryWrapper<Clazz>().in(Clazz::getId, clazzIds)).forEach(
                                    ee -> {
                                        clazzStr.append("[" + ee.getName() + "] ");
                                        if (examResponse.getGradeName().equals("")) {
                                            Grade grade = gradeMapper.selectOne(new LambdaQueryWrapper<Grade>().eq(Grade::getId, ee.getGradeId()));
                                            examResponse.setGradeName(grade.getName());
                                        }
                                    }
                            );
                            examResponse.setClazzName(clazzStr.toString());

                        }
                        List<ExamCourse> examCourses = examCourseMapper.selectList(new LambdaQueryWrapper<ExamCourse>().eq(ExamCourse::getExamId, e.getId()));
                        ArrayList<Integer> examCourseIds = new ArrayList<>();
                        examCourses.forEach(ee -> {
                            examCourseIds.add(ee.getCourseId());
                        });
                        StringBuilder courseStr = new StringBuilder("");
                        courseMapper.selectList(new LambdaQueryWrapper<Course>().in(Course::getId, examCourseIds)).forEach(
                                ee -> {
                                    courseStr.append("[" + ee.getName() + "]");
                                }
                        );
                        examResponse.setCourseName(courseStr.toString());
                    }
            );
        }
        return response;
    }

    @Override
    public InsertResponse insert(ExamInsertRequest request, String token) {
        User user = UserUtil.get(token);
        InsertResponse response = new InsertResponse();
        response.setCode(20001);
        if (user != null && user.getType() == 0) {
            response.setCode(20000);
            Exam exam = new Exam();
            exam.setName(request.getName());
            exam.setRemark(request.getRemark());
            exam.setTime(request.getTime());
            exam.setType(request.getType());
            examMapper.insert(exam);

            if (request.getType() == 0) {
                ExamGrade examGrade = new ExamGrade();
                examGrade.setExamId(exam.getId());
                examGrade.setGradeId(request.getGradeId());
                examGradeMapper.insert(examGrade);

            } else if (request.getType() == 1) {
                request.getClazzIds().forEach(
                        id -> {
                            ExamClazz examClazz = new ExamClazz();
                            examClazz.setExamId(exam.getId());
                            examClazz.setClazzId(id);
                            examClazzMapper.insert(examClazz);
                        }
                );
            }
            request.getCourseIds().forEach(
                    id -> {
                        ExamCourse examCourse = new ExamCourse();
                        examCourse.setExamId(exam.getId());
                        examCourse.setCourseId(id);
                        examCourseMapper.insert(examCourse);
                    }
            );
        }
        return response;
    }

    @Override
    public ExamScoreResponse score(ExamScoreRequest request, String token) {
        User user = UserUtil.get(token);
        ExamScoreResponse response = new ExamScoreResponse();
        List<ExamCourse> examCourses = examCourseMapper.selectList(new LambdaQueryWrapper<ExamCourse>().eq(ExamCourse::getExamId, request.getExamId()));
        ArrayList<Integer> list = new ArrayList<>();
        examCourses.forEach(e -> {
            list.add(e.getCourseId());
        });
        List<Course> courses = courseMapper.selectList(new LambdaQueryWrapper<Course>().in(Course::getId, list));
        ArrayList<String> tableLabels = new ArrayList<>();
        String headerString = "[ {label:'学号',prop:'number'},{label:'姓名',prop:'name'},";
        for (int i = 0; i < courses.size(); i++) {
            tableLabels.add(courses.get(i).getName());
            headerString += "{label:" + "'" + courses.get(i).getName() + "',prop:'prop" + i + "'}";
            if (i != courses.size() - 1) {
                headerString += ",";
            }
        }

        String data = "[";

        List<Student> students = studentMapper
                .selectList(new LambdaQueryWrapper<Student>()
                        .eq(Student::getClazzId, user.getType() == 2
                                ? studentMapper.selectOne(new LambdaQueryWrapper<Student>().eq(Student::getUserId, user.getId())).getClazzId()
                                : request.getClazzId())
                );
        for (int j = 0; j < students.size(); j++) {
            data += "{";
            data += "number:'" + students.get(j).getNumber() + "',";
            data += "name:'" + students.get(j).getName() + "',";
            for (int k = 0; k < courses.size(); k++) {
                Score score = scoreMapper.selectOne(new LambdaQueryWrapper<Score>()
                        .eq(Score::getExamId, request.getExamId())
                        .eq(Score::getStudentId, students.get(j).getId())
                        .eq(Score::getCourseId, courses.get(k).getId())
                );
                data += "prop" + k + ":'" + (score == null ? "成绩未录入" : score.getScore()) + "'";
                if (k != courses.size() - 1) {
                    data += ",";
                }
            }
            data += "}";
            if (j != students.size() - 1) {
                data += ",";
            }
        }

        data += "]";

        headerString += "]";
        response.setCode(20000);
        response.setHeader(headerString);
        response.setData(data);
        return response;
    }

    @Override
    public UpdateResponse update(ExamInsertRequest request, String token) {
        User user = UserUtil.get(token);
        UpdateResponse response = new UpdateResponse();
        response.setCode(20001);
        if (user != null && user.getType() == 0) {
            response.setCode(20000);
            examClazzMapper.delete(new LambdaQueryWrapper<ExamClazz>().eq(ExamClazz::getExamId, request.getId()));
            examGradeMapper.delete(new LambdaQueryWrapper<ExamGrade>().eq(ExamGrade::getExamId, request.getId()));
            examCourseMapper.delete(new LambdaQueryWrapper<ExamCourse>().eq(ExamCourse::getExamId, request.getId()));

            Exam exam = new Exam();
            BeanUtils.copyProperties(request, exam);
            examMapper.update(exam, new LambdaQueryWrapper<Exam>().eq(Exam::getId, exam.getId()));

            if (request.getType() == 0) {
                ExamGrade examGrade = new ExamGrade();
                examGrade.setExamId(request.getId());
                examGrade.setGradeId(request.getGradeId());
                examGradeMapper.insert(examGrade);

            } else if (request.getType() == 1) {
                request.getClazzIds().forEach(
                        id -> {
                            ExamClazz examClazz = new ExamClazz();
                            examClazz.setExamId(request.getId());
                            examClazz.setClazzId(id);
                            examClazzMapper.insert(examClazz);
                        }
                );
            }
            request.getCourseIds().forEach(
                    id -> {
                        ExamCourse examCourse = new ExamCourse();
                        examCourse.setExamId(request.getId());
                        examCourse.setCourseId(id);
                        examCourseMapper.insert(examCourse);
                    }
            );

        }
        return response;
    }

    @Override
    public UpdateResponse delete(List<Exam> id, String token) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (Exam exam : id) {
            ids.add(exam.getId());
        }
        User user = UserUtil.get(token);
        UpdateResponse response = new UpdateResponse();
        if (user != null && user.getType() == 0) {
            scoreMapper.delete(new LambdaQueryWrapper<Score>().in(Score::getExamId, ids));
            examGradeMapper.delete(new LambdaQueryWrapper<ExamGrade>().in(ExamGrade::getExamId, ids));
            examCourseMapper.delete(new LambdaQueryWrapper<ExamCourse>().in(ExamCourse::getExamId, ids));
            examClazzMapper.delete(new LambdaQueryWrapper<ExamClazz>().in(ExamClazz::getExamId, ids));
            examMapper.delete(new LambdaQueryWrapper<Exam>().in(Exam::getId, ids));
            response.setCode(20000);
        }
        return response;
    }
}