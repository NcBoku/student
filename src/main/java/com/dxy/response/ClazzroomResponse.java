package com.dxy.response;

import lombok.Data;

import java.util.List;

@Data
public class ClazzroomResponse {
    private Integer code;
    private List<ExamResponse> exams;
}
