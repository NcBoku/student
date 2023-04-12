package com.dxy.response;

import com.dxy.pojo.Clazz;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class ClazzIdsResponse {
    private int code;
    private HashMap<Integer, List<Clazz>> map;
}
