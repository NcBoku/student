package com.dxy.response;

import lombok.Data;

import java.util.List;

@Data
public class UserExamClazzroomPageResponse {
    private Integer code;
    private String error;
    private Integer total;
    private List<UserExamClazzroomResponse> response;
}
