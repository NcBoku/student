package com.dxy.response;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class ClazzIdsResponse {
    private int code;
    private HashMap<Integer, List<Integer>> map;
}
