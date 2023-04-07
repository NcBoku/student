package com.dxy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class R {
    private int code;
    private Object data;

    public static R success(Object data) {
        return new R(200, data);
    }

    public static R error(Object data) {
        return new R(400, data);
    }
}
