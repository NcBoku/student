package com.dxy.response;

import com.dxy.pojo.Teacher;
import lombok.Data;

import java.util.List;

@Data
public class TeacherPageResponse {
    private Integer code;
    private List<TeacherResponse> teachers;
    private Integer total;
}
