package com.dxy.request;

import com.dxy.pojo.Teacher;
import lombok.Data;

import java.util.List;

@Data
public class TeacherUpdateRequest {
    private Integer id;

    private String number;

    private String name;

    private String sex;

    private String phone;

    private String qq;

    private String photo;

    private List<Integer> courseIds;
}
