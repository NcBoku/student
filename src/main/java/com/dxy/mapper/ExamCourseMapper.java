package com.dxy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dxy.pojo.Course;
import com.dxy.pojo.ExamCourse;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExamCourseMapper extends BaseMapper<ExamCourse> {
}
