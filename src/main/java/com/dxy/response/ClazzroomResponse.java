package com.dxy.response;

import com.dxy.pojo.Clazzroom;
import lombok.Data;

import java.util.List;

@Data
public class ClazzroomResponse {
    private Integer code;
    private Clazzroom clazzRoom;
    private List<ExamResponse> exams;
    private String error;
}
