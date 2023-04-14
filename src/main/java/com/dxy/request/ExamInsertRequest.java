package com.dxy.request;

import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class ExamInsertRequest {
    private Integer id;

    private String name;

    private Date time;

    private String remark;

    private Integer type;

    private Integer gradeId;

    private List<Integer> clazzIds;

    private List<Integer> courseIds;
}
