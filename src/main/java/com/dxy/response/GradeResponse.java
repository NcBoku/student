package com.dxy.response;

import com.dxy.pojo.Course;
import lombok.Data;

import java.util.List;

@Data
public class GradeResponse {
    private Integer id;

    private String name;

    private List<Course> courses;

    private String remark;
}
