package com.dxy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dxy.pojo.GradeCourse;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface GradeCourseMapper extends BaseMapper<GradeCourse> {
}
