package com.dxy.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dxy.pojo.Student;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {
}
