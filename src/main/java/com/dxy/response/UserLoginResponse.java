package com.dxy.response;

import lombok.Data;

import java.lang.ref.PhantomReference;

@Data
public class UserLoginResponse {
    private Integer code;

    private String token;

    private Integer type;
}
