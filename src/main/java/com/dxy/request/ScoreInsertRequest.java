package com.dxy.request;

import lombok.Data;

@Data
public class ScoreInsertRequest {
    private Integer examId;

    private Integer studentId;

    private Integer courseId;

    private Integer score;
}
