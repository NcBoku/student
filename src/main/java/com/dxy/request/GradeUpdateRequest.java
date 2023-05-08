package com.dxy.request;

import lombok.Data;

import java.util.List;

@Data
public class GradeUpdateRequest {
    private Integer id;
    private String name;
    private List<Integer> courses;
    private String remark;
}
