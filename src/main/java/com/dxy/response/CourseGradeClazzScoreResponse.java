package com.dxy.response;

import com.dxy.pojo.*;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class CourseGradeClazzScoreResponse {
    private Integer code;
    private HashMap<Integer, HashMap<Integer, List<Score>>> map;
    private List<Grade> gradeMap;
    private List<Course> courseMap;
    private List<Clazz> clazzMap;
    private List<Student> students;
}
