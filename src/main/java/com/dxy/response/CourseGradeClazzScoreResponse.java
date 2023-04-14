package com.dxy.response;

import com.dxy.pojo.Clazz;
import com.dxy.pojo.Course;
import com.dxy.pojo.Grade;
import com.dxy.pojo.Score;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class CourseGradeClazzScoreResponse {
    private Integer code;
    private HashMap<Integer,HashMap<Integer, HashMap<Integer, List<Score>>>> map;
    private HashMap<Integer, Grade> gradeMap;
    private HashMap<Integer, Course> courseMap;
    private HashMap<Integer, Clazz> clazzMap;
}
