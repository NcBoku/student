package com.dxy.response;

import com.dxy.pojo.Clazz;
import lombok.Data;

import java.util.List;

@Data
public class ClazzPageResponse {
    private Integer code;

    private List<ClazzResponse> clazz;

    private Integer totalPage;
}
