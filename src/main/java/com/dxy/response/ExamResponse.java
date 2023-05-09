package com.dxy.response;

import com.dxy.pojo.Student;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamResponse{
    private Integer id;

    private String name;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date end;

    private String remark;

    private Integer type;

    private String gradeName;

    private String clazzName;

    private String courseName;

    private String clazzroomName;

    private String teacherNames;

    private String studentNames;

    private List<StudentResponse> students;
    
    private String teachers;
}