package com.dxy.response;

import com.dxy.pojo.Grade;
import lombok.Data;

import java.util.List;

@Data
public class GradesResponse {
    private Integer code;
    private List<Grade> grades;
}
