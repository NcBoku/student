package com.dxy.response;

import com.dxy.pojo.Course;
import lombok.Data;

import java.util.List;

@Data
public class ClazzroomPageResponse {
    private Integer code;
    private List<ClazzroomResponse> clazzroomResponses;
    private Integer total;
}
