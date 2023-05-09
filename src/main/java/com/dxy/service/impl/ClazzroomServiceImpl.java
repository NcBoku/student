package com.dxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.*;
import com.dxy.pojo.*;
import com.dxy.request.PageGetRequest;
import com.dxy.response.*;
import com.dxy.service.ClazzService;
import com.dxy.service.ClazzroomService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ClazzroomServiceImpl extends ServiceImpl<ClazzroomMapper, Clazzroom> implements ClazzroomService {
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

    @Autowired
    private GradeCourseMapper gradeCourseMapper;

    @Autowired
    private ExamClazzroomMapper examClazzroomMapper;

    @Autowired
    private ClazzroomMapper clazzroomMapper;


    @Transactional
    @Override
    public void updateInfo() {

    }

    @Transactional
    @Override
    public List<Clazzroom> getRestClazzroom(java.util.Date start, Date end) {
        List<ExamClazzroom> usedClazzroom = examClazzroomMapper.selectList(new LambdaQueryWrapper<ExamClazzroom>().
                eq(ExamClazzroom::getIsDeleted, false)
                .and(
                        o -> o.and(
                                o1 -> o1.le(ExamClazzroom::getStart, start)
                                        .ge(ExamClazzroom::getEnd, end)
                        ).or(
                                o1 -> o1.ge(ExamClazzroom::getStart, start)
                                        .ge(ExamClazzroom::getEnd, end)
                                        .le(ExamClazzroom::getStart, end)
                        ).or(
                                o1 -> o1.le(ExamClazzroom::getStart, start)
                                        .ge(ExamClazzroom::getEnd, start)
                                        .le(ExamClazzroom::getEnd, end)
                        ).or(
                                o1 -> o1.ge(ExamClazzroom::getStart, start)
                                        .le(ExamClazzroom::getEnd, end)
                        )
                )
        );
        List<Integer> ids = new ArrayList<>();
        usedClazzroom.forEach(
                e -> {
                    ids.add(e.getClazzroomId());
                }
        );
        if (ids.size() == 0) {
            return clazzroomMapper.selectList(null);
        }
        List<Clazzroom> clazzrooms = clazzroomMapper.selectList(new LambdaQueryWrapper<Clazzroom>().notIn(Clazzroom::getId, ids));

        return clazzrooms;
    }

    @Override
    @Transactional
    public List<Clazz> getNotRestClazz(Date start, Date end) {
        System.out.println("*****");
        List<ExamClazzroom> usedClazzroom = examClazzroomMapper.selectList(new LambdaQueryWrapper<ExamClazzroom>().
                eq(ExamClazzroom::getIsDeleted, false)
                .and(
                        o -> o.and(
                                o1 -> o1.le(ExamClazzroom::getStart, start)
                                        .ge(ExamClazzroom::getEnd, end)
                        ).or(
                                o1 -> o1.ge(ExamClazzroom::getStart, start)
                                        .ge(ExamClazzroom::getEnd, end)
                                        .le(ExamClazzroom::getStart, end)
                        ).or(
                                o1 -> o1.le(ExamClazzroom::getStart, start)
                                        .ge(ExamClazzroom::getEnd, start)
                                        .le(ExamClazzroom::getEnd, end)
                        ).or(
                                o1 -> o1.ge(ExamClazzroom::getStart, start)
                                        .le(ExamClazzroom::getEnd, end)
                        )
                )
        );

        List<Integer> ids = new ArrayList<>();
        usedClazzroom.forEach(
                e -> {
                    ids.add(e.getExamId());
                }
        );
        if (ids.size() == 0) {
            return new ArrayList<>();
        }

        List<Exam> runningExam = examMapper.selectList(new LambdaQueryWrapper<Exam>().in(Exam::getId, ids));
        ids.clear();
        runningExam.forEach(e -> {
            ids.add(e.getId());
        });

        List<Score> scores = scoreMapper.selectList(new LambdaQueryWrapper<Score>().in(Score::getExamId, ids));
        ids.clear();
        scores.forEach(e -> {
            if (!ids.contains(e.getClazzId())) {
                ids.add(e.getClazzId());
            }
        });

        List<Clazz> clazzes = clazzMapper.selectList(new LambdaQueryWrapper<Clazz>().in(Clazz::getId, ids));

        return clazzes;
    }

    @Transactional
    @Override
    public List<Teacher> getNotRestTeacher(Date start, Date end) {
        List<ExamClazzroom> usedClazzroom = examClazzroomMapper.selectList(new LambdaQueryWrapper<ExamClazzroom>().
                eq(ExamClazzroom::getIsDeleted, false)
                .and(
                        o -> o.and(
                                o1 -> o1.le(ExamClazzroom::getStart, start)
                                        .ge(ExamClazzroom::getEnd, end)
                        ).or(
                                o1 -> o1.ge(ExamClazzroom::getStart, start)
                                        .ge(ExamClazzroom::getEnd, end)
                                        .le(ExamClazzroom::getStart, end)
                        ).or(
                                o1 -> o1.le(ExamClazzroom::getStart, start)
                                        .ge(ExamClazzroom::getEnd, start)
                                        .le(ExamClazzroom::getEnd, end)
                        ).or(
                                o1 -> o1.ge(ExamClazzroom::getStart, start)
                                        .le(ExamClazzroom::getEnd, end)
                        )
                )
        );

        List<Integer> ids = new ArrayList<>();
        usedClazzroom.forEach(
                e -> {
                    ids.add(e.getExamId());
                }
        );
        if (ids.size() == 0) {
            return new ArrayList<>();
        }

        ids.clear();
        usedClazzroom.forEach(e -> {
            String[] split = e.getTeachers().split(",");
            for (String s : split) {
                ids.add(Integer.parseInt(s));
            }
        });

        List<Teacher> teachers = teacherMapper.selectList(new LambdaQueryWrapper<Teacher>().in(Teacher::getId, ids));
        return teachers;
    }

    @Transactional
    @Override
    public ClazzroomResponse getInfo(Integer id) {
        ClazzroomResponse response = new ClazzroomResponse();
        response.setCode(20000);
        List<ExamClazzroom> examClazzroom = examClazzroomMapper.selectList(new LambdaQueryWrapper<ExamClazzroom>()
                .eq(ExamClazzroom::getClazzroomId, id)
                .eq(ExamClazzroom::getIsDeleted, false)
        );
        ArrayList<Integer> list = new ArrayList<>();
        examClazzroom.forEach(e -> {
            list.add(e.getExamId());
        });
        response.setExams(new ArrayList<>());
        if (list.size() == 0) {
            response.setError("该考场最近没有考试安排");
            return response;
        }
        List<Exam> exams = examMapper.selectList(new LambdaQueryWrapper<Exam>().in(Exam::getId, list));
        exams.forEach(e -> {
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

            StringBuilder clazzRoom = new StringBuilder("");
            List<ExamClazzroom> examClazzrooms = examClazzroomMapper.selectList(new LambdaQueryWrapper<ExamClazzroom>().eq(ExamClazzroom::getExamId, e.getId()));
            ArrayList<Integer> clazzRoomIds = new ArrayList<>();
            examClazzrooms.forEach(ee -> {
                clazzRoomIds.add(ee.getClazzroomId());
            });
            clazzroomMapper.selectList(new LambdaQueryWrapper<Clazzroom>().in(Clazzroom::getId, clazzRoomIds)).forEach(ee -> {
                clazzRoom.append("[" + ee.getName() + "]");
            });
            examResponse.setClazzroomName(clazzRoom.toString());
        });
        return response;
    }

    @Transactional
    @Override
    public ClazzroomPageResponse getList(PageGetRequest request) {
        Page<Clazzroom> page = clazzroomMapper.selectPage(new Page<Clazzroom>(request.getPage(), request.getSize()), new LambdaQueryWrapper<Clazzroom>().orderByDesc(Clazzroom::getId));
        ClazzroomPageResponse response = new ClazzroomPageResponse();
        response.setTotal((int) page.getTotal());
        response.setCode(20000);
        response.setClazzroomResponses(new ArrayList<>());
        page.getRecords().forEach(e -> {
            response.getClazzroomResponses().add(getInfo(e.getId()));
        });
        return response;
    }

    @Transactional
    @Override
    public InsertResponse insert(Clazzroom clazzroom) {
        InsertResponse response = new InsertResponse();
        response.setCode(20000);
        response.setError(clazzroomMapper.insert(clazzroom) == 1 ? "插入成功" : "插入失败");
        return response;
    }

    @Transactional
    @Override
    public UpdateResponse update(Clazzroom clazzroom) {
        UpdateResponse response = new UpdateResponse();
        response.setCode(20000);
        response.setError(clazzroomMapper.update(clazzroom, new LambdaQueryWrapper<Clazzroom>().eq(Clazzroom::getId, clazzroom.getId())) == 1 ?
                "插入成功" : "插入失败");
        return response;
    }

    @Transactional
    @Override
    public UpdateResponse delete(Integer id) {
         examClazzroomMapper.delete(new LambdaQueryWrapper<ExamClazzroom>().eq(ExamClazzroom::getClazzroomId, id));
         clazzroomMapper.delete(new LambdaQueryWrapper<Clazzroom>().eq(Clazzroom::getId, id));
        UpdateResponse response = new UpdateResponse();
        response.setCode(20000);
        response.setError("删除成功");
        return response;
    }
}
