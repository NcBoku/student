package com.dxy.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamResponse{
    private Integer id;

    private String name;

    private Date time;

    private String remark;

    private Integer type;

    private String gradeName;

    private String clazzName;

    private String courseName;
}