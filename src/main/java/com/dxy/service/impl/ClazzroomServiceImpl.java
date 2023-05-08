package com.dxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.*;
import com.dxy.pojo.*;
import com.dxy.service.ClazzService;
import com.dxy.service.ClazzroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ClazzroomServiceImpl extends ServiceImpl<ClazzroomMapper, Clazzroom> implements ClazzroomService {
    @Autowired
    private ClazzroomMapper clazzroomMapper;

    @Autowired
    private ExamClazzroomMapper examClazzroomMapper;

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private ScoreMapper scoreMapper;


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
                                        .ge(ExamClazzroom::getEnd,end)
                                        .le(ExamClazzroom::getStart, end)
                        ).or(
                                o1 -> o1.le(ExamClazzroom::getStart, start)
                                        .ge(ExamClazzroom::getEnd,start)
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
                                        .ge(ExamClazzroom::getEnd,end)
                                        .le(ExamClazzroom::getStart, end)
                        ).or(
                                o1 -> o1.le(ExamClazzroom::getStart, start)
                                        .ge(ExamClazzroom::getEnd,start)
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
        runningExam.forEach(e->{
            ids.add(e.getId());
        });

        List<Score> scores = scoreMapper.selectList(new LambdaQueryWrapper<Score>().in(Score::getExamId, ids));
        ids.clear();
        scores.forEach(e->{
            if (!ids.contains(e.getClazzId())){
                ids.add(e.getClazzId());
            }
        });

        List<Clazz> clazzes = clazzMapper.selectList(new LambdaQueryWrapper<Clazz>().in(Clazz::getId, ids));

        return clazzes;
    }

    @Override
    public List<Clazz> getRestTeacher(Date start, Date end) {
        return null;
    }
}
