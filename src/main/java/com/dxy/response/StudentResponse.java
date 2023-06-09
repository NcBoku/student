package com.dxy.response;

import com.dxy.pojo.Student;
import lombok.Data;

@Data
public class StudentResponse {
    private Integer code;

    private Integer id;

    private String number;

    private String name;

    private String sex;

    private String phone;

    private String qq;

    private String clazzName;

    private Integer clazzId;

    private String grade;
}
