package com.dxy.response;

import com.dxy.pojo.Score;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class ExamScoreResponse {
    private Integer code;

    private String header;

    private String data;
}
