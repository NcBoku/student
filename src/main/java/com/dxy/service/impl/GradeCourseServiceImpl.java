package com.dxy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxy.mapper.GradeCourseMapper;
import com.dxy.pojo.GradeCourse;
import com.dxy.service.GradeCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradeCourseServiceImpl extends ServiceImpl<GradeCourseMapper, GradeCourse> implements GradeCourseService {

    @Autowired
    private GradeCourseMapper gradeCourseMapper;
}
