package com.dxy.response;

import com.dxy.pojo.Teacher;
import lombok.Data;

import java.util.List;

@Data
public class TeacherPageResponse {
    private Integer code;
    private List<Teacher> teachers;
    private Integer total;
}
