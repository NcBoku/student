package com.dxy.response;

import com.dxy.pojo.Course;
import lombok.Data;

import java.util.List;

@Data
public class CoursePageResponse {
    private Integer code;
    private List<Course> courses;
    private Integer total;
}
