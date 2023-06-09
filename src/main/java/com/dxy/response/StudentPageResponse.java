package com.dxy.response;

import com.dxy.pojo.Student;
import lombok.Data;

import java.util.List;

@Data
public class StudentPageResponse {
    private Integer code;
    private List<StudentResponse> students;
    private Integer total;
}
