package com.dxy.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.GradeCourseMapper;
import com.dxy.mapper.GradeMapper;
import com.dxy.pojo.Grade;
import com.dxy.pojo.GradeCourse;
import com.dxy.service.GradeCourseService;
import com.dxy.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements GradeService {

    @Autowired
    private GradeMapper gradeMapper;
}