package com.dxy.response;

import com.dxy.pojo.Course;
import com.dxy.pojo.Teacher;
import lombok.Data;

import java.util.List;

@Data
public class TeacherResponse {
    private Integer code;

    private Integer id;

    private String number;

    private String name;

    private String sex;

    private String phone;

    private String qq;

    private String photo;

    private Integer userId;

    private List<Course> courses;
}
