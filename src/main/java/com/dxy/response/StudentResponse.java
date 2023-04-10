package com.dxy.response;

import com.dxy.pojo.Student;
import lombok.Data;

@Data
public class StudentResponse {
    private Integer code;
    private Student student;
}
