package com.dxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.*;
import com.dxy.pojo.*;
import com.dxy.request.ScoreInsertRequest;
import com.dxy.response.CourseGradeClazzScoreResponse;
import com.dxy.response.InsertResponse;
import com.dxy.response.UpdateResponse;
import com.dxy.service.ScoreService;
import com.dxy.util.UserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ScoreServiceImpl extends ServiceImpl<ScoreMapper, Score> implements ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private TeacherCourseMapper teacherCourseMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private GradeCourseMapper gradeCourseMapper;

    @Autowired
    private GradeMapper gradeMapper;

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private ExamCourseMapper examCourseMapper;

    @Override
    public InsertResponse insert(ScoreInsertRequest request) {
        InsertResponse response = new InsertResponse();
        Score score = new Score();
        Integer studentId = request.getStudentId();
        Student student = studentMapper.selectOne(new LambdaQueryWrapper<Student>().eq(Student::getId, studentId));
        BeanUtils.copyProperties(request, score);
        score.setClazzId(student.getClazzId());
        response.setCode(scoreMapper.insert(score) == 1 ? 20000 : 20001);
        return response;
    }

    @Override
    public CourseGradeClazzScoreResponse courseClazzScore(String examId, String token) {
        User user = UserUtil.get(token);
        CourseGradeClazzScoreResponse response = new CourseGradeClazzScoreResponse();
        response.setCode(20000);
        response.setClazzMap(new ArrayList<>());
        response.setCourseMap(new ArrayList<>());
        response.setGradeMap(new ArrayList<>());
        response.setMap(new HashMap<>());
        response.setStudents(new ArrayList<>());
        Teacher teacher = teacherMapper.selectOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getUserId, user.getId()));
        List<TeacherCourse> teacherCourses = teacherCourseMapper.selectList(new LambdaQueryWrapper<TeacherCourse>().eq(TeacherCourse::getTeacherId, teacher.getId()));
        ArrayList<Integer> cids = new ArrayList<>();
        teacherCourses.forEach(e -> {
            cids.add(e.getCourseId());
        });
        List<ExamCourse> examCourses = examCourseMapper.selectList(new LambdaQueryWrapper<ExamCourse>()
                .eq(ExamCourse::getExamId, examId)
                .in(ExamCourse::getCourseId, cids)
        );

        examCourses.forEach(
                t -> {
                    Course course = courseMapper.selectOne(new LambdaQueryWrapper<Course>().eq(Course::getId, t.getCourseId()));
                    response.getCourseMap().add(course);
                    response.getMap().put(t.getCourseId(), new HashMap<>());

                    List<GradeCourse> gradeCourses = gradeCourseMapper.selectList(new LambdaQueryWrapper<GradeCourse>().eq(GradeCourse::getCourseId, course.getId()));
                    gradeCourses.forEach(
                            g -> {
                                Grade grade = gradeMapper.selectOne(new LambdaQueryWrapper<Grade>().eq(Grade::getId, g.getGradeId()));
                                if (grade == null) {
                                    return;
                                }
                                response.getGradeMap().add(grade);
                                List<Clazz> clazz = clazzMapper.selectList(new LambdaQueryWrapper<Clazz>().eq(Clazz::getGradeId, g.getGradeId()));
                                clazz.forEach(c -> {
                                    response.getClazzMap().add(c);
                                    List<Score> scoreList = scoreMapper.selectList(new LambdaQueryWrapper<Score>()
                                            .eq(Score::getExamId, examId)
                                            .eq(Score::getCourseId, t.getCourseId())
                                            .eq(Score::getClazzId, c.getId())
                                    );
                                    ArrayList<Integer> sids = new ArrayList<>();
                                    scoreList.forEach(s -> {
                                        sids.add(s.getStudentId());
                                    });
                                    if (sids.size() != 0) {
                                        List<Student> students = studentMapper.selectList(new LambdaQueryWrapper<Student>().in(Student::getId, sids));
                                        response.setStudents(students);
                                        response.getMap().get(t.getCourseId()).put(c.getId(), scoreList);
                                    } else {
                                        response.getClazzMap().remove(response.getClazzMap().size() - 1);
                                        response.getMap().get(t.getCourseId()).remove(g.getGradeId());
                                    }

                                });
                            }
                    );
                }
        );
        return response;
    }

    @Override
    public UpdateResponse update(List<Score> scoreList, String token) {
        UpdateResponse response = new UpdateResponse();
        response.setCode(20001);
        if(UserUtil.get(token).getType()==1){
            response.setCode(20000);
            scoreList.forEach(e->{
                scoreMapper.update(e,new LambdaQueryWrapper<Score>().eq(Score::getId,e.getId()));
            });
        }
        return response;
    }
}
