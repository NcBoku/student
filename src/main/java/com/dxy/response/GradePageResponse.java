package com.dxy.response;

import com.dxy.pojo.Grade;
import lombok.Data;

import java.util.List;

@Data
public class GradePageResponse {
    private Integer code;

    private List<GradeResponse> grade;

    private Integer totalPage;
}
