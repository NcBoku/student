package com.dxy.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserExamClazzroomResponse {
    private String clazzroomName;
    private String examName;
    private String location;
    private List<StudentResponse> students;
    private String teachers;
    private String courses;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date start;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date end;
    private Integer id;
}
