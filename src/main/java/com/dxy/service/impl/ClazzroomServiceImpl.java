package com.dxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.ClazzMapper;
import com.dxy.mapper.ClazzroomMapper;
import com.dxy.mapper.ExamClazzMapper;
import com.dxy.mapper.ExamClazzroomMapper;
import com.dxy.pojo.Clazz;
import com.dxy.pojo.Clazzroom;
import com.dxy.pojo.ExamClazzroom;
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
                        ).or().and(
                                o1 -> o1.ge(ExamClazzroom::getStart, start)
                                        .le(ExamClazzroom::getEnd, end)
                        ).or().and(
                                o1 -> o1.le(ExamClazzroom::getStart, start)
                                        .le(ExamClazzroom::getEnd, end)
                        ).or().and(
                                o1 -> o1.ge(ExamClazzroom::getStart, start)
                                        .ge(ExamClazzroom::getEnd, end)
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
            return null;
        }
        List<Clazzroom> clazzrooms = clazzroomMapper.selectList(new LambdaQueryWrapper<Clazzroom>().notIn(Clazzroom::getId, ids));

        return clazzrooms;
    }
}
