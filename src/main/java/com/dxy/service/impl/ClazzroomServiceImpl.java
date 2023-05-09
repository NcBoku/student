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
import com.dxy.util.UserUtil;
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

    @Autowired
    private UserMapper userMapper;


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
        Clazzroom clazzroom = clazzroomMapper.selectOne(new LambdaQueryWrapper<Clazzroom>().eq(Clazzroom::getId, id));
        response.setClazzRoom(clazzroom);
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
                        courseStr.append(ee.getName() + ",");
                    }
            );
            courseStr.deleteCharAt(courseStr.length() - 1);
            examResponse.setCourseName(courseStr.toString());

            StringBuilder clazzRoom = new StringBuilder("");
            StringBuilder teacherNames = new StringBuilder("");
            StringBuilder studentNames = new StringBuilder("");
            List<ExamClazzroom> examClazzrooms = examClazzroomMapper.selectList(new LambdaQueryWrapper<ExamClazzroom>().eq(ExamClazzroom::getExamId, e.getId()));
            ArrayList<Integer> clazzRoomIds = new ArrayList<>();
            ArrayList<Integer> tids = new ArrayList<>();
            ArrayList<Integer> sids = new ArrayList<>();
            examClazzrooms.forEach(ee -> {
                clazzRoomIds.add(ee.getClazzroomId());
                for (String s : ee.getTeachers().split(",")) {
                    tids.add(Integer.parseInt(s));
                }
                for (String s : ee.getStudents().split(",")) {
                    sids.add(Integer.parseInt(s));
                }
            });
            teacherMapper.selectList(new LambdaQueryWrapper<Teacher>().in(Teacher::getId, tids)).forEach(ee -> {
                teacherNames.append(ee.getName() + ",");
            });
            List<Student> students = studentMapper.selectList(new LambdaQueryWrapper<Student>().in(Student::getId, sids).orderByDesc(Student::getId));
            ArrayList<StudentResponse> studentResponses = new ArrayList<>();
            students.forEach(ee -> {
                studentNames.append(ee.getName() + ",");
                Clazz clazz = clazzMapper.selectOne(new LambdaQueryWrapper<Clazz>().eq(Clazz::getId, ee.getClazzId()));
                StudentResponse studentResponse = new StudentResponse();
                BeanUtils.copyProperties(ee, studentResponse);
                studentResponse.setClazzName(clazz.getName());
                studentResponses.add(studentResponse);
            });
            clazzroomMapper.selectList(new LambdaQueryWrapper<Clazzroom>().in(Clazzroom::getId, clazzRoomIds)).forEach(ee -> {
                clazzRoom.append(ee.getName() + ",");
            });
            teacherNames.deleteCharAt(teacherNames.length() - 1);
            examResponse.setTeacherNames(teacherNames.toString());
            clazzRoom.deleteCharAt(clazzRoom.length() - 1);
            examResponse.setClazzroomName(clazzRoom.toString());
            studentNames.deleteCharAt(studentNames.length() - 1);
            examResponse.setStudentNames(studentNames.toString());
            examResponse.setStudents(studentResponses);
        });
        return response;
    }

    @Transactional
    @Override
    public ClazzroomPageResponse getList(PageGetRequest request) {

        Page<Clazzroom> page = null;
        if (request.getKeyword() != null && !"".equals(request.getKeyword())) {
            page = clazzroomMapper.selectPage(new Page<Clazzroom>(request.getPage(), request.getSize()), new LambdaQueryWrapper<Clazzroom>()
                    .orderByAsc(Clazzroom::getId)
                    .and(
                            o -> o.like(Clazzroom::getName, request.getKeyword())
                                    .or()
                                    .like(Clazzroom::getLocation, request.getKeyword())
                    )
            );
        } else {
            page = clazzroomMapper.selectPage(new Page<Clazzroom>(request.getPage(), request.getSize()), new LambdaQueryWrapper<Clazzroom>().orderByAsc(Clazzroom::getId));
        }
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
                "修改" : "修改");
        return response;
    }

    @Transactional
    public UpdateResponse deleteOne(Integer id) {
        List<ExamClazzroom> examClazzrooms = examClazzroomMapper.selectList(new LambdaQueryWrapper<ExamClazzroom>().eq(ExamClazzroom::getClazzroomId, id));
        ArrayList<Integer> eids = new ArrayList<>();
        examClazzrooms.forEach(e -> {
            eids.add(e.getExamId());
        });
        examClazzroomMapper.delete(new LambdaQueryWrapper<ExamClazzroom>().eq(ExamClazzroom::getClazzroomId, id));
        clazzroomMapper.delete(new LambdaQueryWrapper<Clazzroom>().eq(Clazzroom::getId, id));
        UpdateResponse response = new UpdateResponse();
        response.setCode(20000);
        response.setError("删除成功");
        return del(eids);
    }

    @Transactional
    @Override
    public UpdateResponse delete(List<Integer> id) {
        for (Integer integer : id) {
            deleteOne(integer);
        }
        UpdateResponse response = new UpdateResponse();
        response.setCode(20000);
        response.setError("删除成功");
        return response;
    }

    @Override
    public UserExamClazzroomPageResponse student(PageGetRequest request, Integer id) {
        Student student = studentMapper.selectOne(new LambdaQueryWrapper<Student>().eq(Student::getUserId, id));
        int iid = student.getId();
        ArrayList<Integer> ecids = new ArrayList<>();
        examClazzroomMapper.selectList(null).forEach(e -> {
            for (String s : e.getStudents().split(",")) {
                int i = Integer.parseInt(s);
                System.out.println("学生" + iid + " 解析出的" + i);
                if (iid == i) {
                    ecids.add(e.getId());
                }
            }
        });
        LambdaQueryWrapper<ExamClazzroom> examClazzroomLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ecids.size() == 0) {
            UserExamClazzroomPageResponse response = new UserExamClazzroomPageResponse();
            response.setCode(20000);
            return response;
        }
        examClazzroomLambdaQueryWrapper
                .in(ExamClazzroom::getId, ecids)
                .orderByDesc(ExamClazzroom::getStart);
        Page<ExamClazzroom> page = examClazzroomMapper
                .selectPage(new Page<>(request.getPage(), request.getSize()), examClazzroomLambdaQueryWrapper);
        System.out.println("学生有关系的考试");
        System.out.println(page.getRecords());
        UserExamClazzroomPageResponse response = new UserExamClazzroomPageResponse();
        response.setTotal((int) page.getTotal());
        response.setCode(20000);
        response.setResponse(new ArrayList<>());
        page.getRecords().forEach(e -> {
            UserExamClazzroomResponse r = new UserExamClazzroomResponse();

            r.setExamName(examMapper.selectOne(new LambdaQueryWrapper<Exam>().eq(Exam::getId, e.getExamId())).getName());
            r.setEnd(e.getEnd());
            r.setStart(e.getStart());
            Clazzroom clazzroom = clazzroomMapper.selectOne(new LambdaQueryWrapper<Clazzroom>().eq(Clazzroom::getId, e.getClazzroomId()));
            r.setClazzroomName(clazzroom.getName());
            r.setLocation(clazzroom.getLocation());

            StringBuilder courseNames = new StringBuilder("");
            ArrayList<Integer> cid = new ArrayList<>();
            examCourseMapper.selectList(new LambdaQueryWrapper<ExamCourse>().eq(ExamCourse::getExamId, e.getExamId())).forEach(ee -> {
                cid.add(ee.getCourseId());
            });
            courseMapper.selectList(new LambdaQueryWrapper<Course>().in(Course::getId, cid)).forEach(ee -> {
                courseNames.append(ee.getName() + ",");
            });
            courseNames.deleteCharAt(courseNames.length() - 1);
            r.setCourses(courseNames.toString());


            ArrayList<Integer> sid = new ArrayList<>();
            for (String s : e.getStudents().split(",")) {
                if (Integer.parseInt(s) == student.getId()) {
                    sid.add(Integer.parseInt(s));
                }
            }
            List<Student> students = studentMapper.selectList(new LambdaQueryWrapper<Student>().in(Student::getId, sid).orderByDesc(Student::getId));
            r.setStudents(new ArrayList<>());
            students.forEach(ee -> {
                StudentResponse studentResponse = new StudentResponse();
                BeanUtils.copyProperties(ee, studentResponse);
                studentResponse.setClazzName(clazzMapper.selectOne(new LambdaQueryWrapper<Clazz>().eq(Clazz::getId, ee.getClazzId())).getName());
                r.getStudents().add(studentResponse);
            });

            ArrayList<Integer> tid = new ArrayList<>();
            StringBuilder teacherNames = new StringBuilder("");
            for (String s : e.getTeachers().split(",")) {
                tid.add(Integer.parseInt(s));
            }
            List<Teacher> teachers = teacherMapper.selectList(new LambdaQueryWrapper<Teacher>().in(Teacher::getId, tid));
            teachers.forEach(ee -> {
                teacherNames.append(ee.getName() + ",");
            });
            teacherNames.deleteCharAt(teacherNames.length() - 1);

            r.setTeachers(teacherNames.toString());

            response.getResponse().add(r);
        });
        return response;
    }

    @Override
    public UserExamClazzroomPageResponse teacher(PageGetRequest request, Integer id) {
        Teacher teacher = teacherMapper.selectOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getUserId, id));
        int iid = teacher.getId();
        ArrayList<Integer> ecids = new ArrayList<>();
        examClazzroomMapper.selectList(null).forEach(e -> {
            for (String s : e.getTeachers().split(",")) {
                int i = Integer.parseInt(s);
                System.out.println("老师" + iid + " 解析出的" + i);
                if (iid == i) {
                    ecids.add(e.getId());
                }
            }
        });
        LambdaQueryWrapper<ExamClazzroom> examClazzroomLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ecids.size() == 0) {
            UserExamClazzroomPageResponse response = new UserExamClazzroomPageResponse();
            response.setCode(20000);
            return response;
        }
        examClazzroomLambdaQueryWrapper
                .in(ExamClazzroom::getId, ecids)
                .orderByDesc(ExamClazzroom::getStart);
        Page<ExamClazzroom> page = examClazzroomMapper
                .selectPage(new Page<>(request.getPage(), request.getSize()), examClazzroomLambdaQueryWrapper);
        System.out.println("老师有关系的考试");
        System.out.println(page.getRecords());
        UserExamClazzroomPageResponse response = new UserExamClazzroomPageResponse();
        response.setTotal((int) page.getTotal());
        response.setCode(20000);
        response.setResponse(new ArrayList<>());
        page.getRecords().forEach(e -> {
            UserExamClazzroomResponse r = new UserExamClazzroomResponse();

            r.setExamName(examMapper.selectOne(new LambdaQueryWrapper<Exam>().eq(Exam::getId, e.getExamId())).getName());
            r.setEnd(e.getEnd());
            r.setStart(e.getStart());
            Clazzroom clazzroom = clazzroomMapper.selectOne(new LambdaQueryWrapper<Clazzroom>().eq(Clazzroom::getId, e.getClazzroomId()));
            r.setClazzroomName(clazzroom.getName());
            r.setLocation(clazzroom.getLocation());

            StringBuilder courseNames = new StringBuilder("");
            ArrayList<Integer> cid = new ArrayList<>();
            examCourseMapper.selectList(new LambdaQueryWrapper<ExamCourse>().eq(ExamCourse::getExamId, e.getExamId())).forEach(ee -> {
                cid.add(ee.getCourseId());
            });
            courseMapper.selectList(new LambdaQueryWrapper<Course>().in(Course::getId, cid)).forEach(ee -> {
                courseNames.append(ee.getName() + ",");
            });
            courseNames.deleteCharAt(courseNames.length() - 1);
            r.setCourses(courseNames.toString());


            ArrayList<Integer> sid = new ArrayList<>();
            for (String s : e.getStudents().split(",")) {
                sid.add(Integer.parseInt(s));
            }
            List<Student> students = studentMapper.selectList(new LambdaQueryWrapper<Student>().in(Student::getId, sid).orderByDesc(Student::getId));
            r.setStudents(new ArrayList<>());
            students.forEach(ee -> {
                StudentResponse studentResponse = new StudentResponse();
                BeanUtils.copyProperties(ee, studentResponse);
                studentResponse.setClazzName(clazzMapper.selectOne(new LambdaQueryWrapper<Clazz>().eq(Clazz::getId, ee.getClazzId())).getName());
                r.getStudents().add(studentResponse);
            });

            ArrayList<Integer> tid = new ArrayList<>();
            StringBuilder teacherNames = new StringBuilder("");
            for (String s : e.getTeachers().split(",")) {
                tid.add(Integer.parseInt(s));
            }
            List<Teacher> teachers = teacherMapper.selectList(new LambdaQueryWrapper<Teacher>().in(Teacher::getId, tid));
            teachers.forEach(ee -> {
                teacherNames.append(ee.getName() + ",");
            });
            teacherNames.deleteCharAt(teacherNames.length() - 1);

            r.setTeachers(teacherNames.toString());

            response.getResponse().add(r);
        });
        return response;
    }

    @Transactional
    public UpdateResponse del(List<Integer> ids) {
        if (ids.size() == 0) {
            return null;
        }
        UpdateResponse response = new UpdateResponse();
        examClazzroomMapper.delete(new LambdaQueryWrapper<ExamClazzroom>().in(ExamClazzroom::getExamId, ids));
        scoreMapper.delete(new LambdaQueryWrapper<Score>().in(Score::getExamId, ids));
        examGradeMapper.delete(new LambdaQueryWrapper<ExamGrade>().in(ExamGrade::getExamId, ids));
        examCourseMapper.delete(new LambdaQueryWrapper<ExamCourse>().in(ExamCourse::getExamId, ids));
        examClazzMapper.delete(new LambdaQueryWrapper<ExamClazz>().in(ExamClazz::getExamId, ids));
        examMapper.delete(new LambdaQueryWrapper<Exam>().in(Exam::getId, ids));
        response.setCode(20000);

        return response;
    }
}
