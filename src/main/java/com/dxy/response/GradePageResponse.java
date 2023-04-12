package com.dxy.response;

import com.dxy.pojo.Grade;
import lombok.Data;

import java.util.List;

@Data
public class GradePageResponse {
    private Integer code;

    private List<Grade> grade;

    private Integer totalPage;
}
