package com.dxy.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dxy.pojo.Student;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {
}
