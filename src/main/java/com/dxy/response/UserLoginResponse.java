package com.dxy.response;

import lombok.Data;

@Data
public class UserLoginResponse {
    private String token;
    private Integer type;
}
