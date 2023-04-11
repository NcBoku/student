package com.dxy.request;

import com.dxy.pojo.Student;
import lombok.Data;

@Data
public class StudentUpdateRequest {
    private String name;

    private String sex;

    private String phone;

    private String qq;

}
