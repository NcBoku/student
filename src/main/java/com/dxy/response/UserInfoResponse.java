package com.dxy.response;

import lombok.Data;

@Data
public class UserInfoResponse {
    private Integer code;

    private String roles;

    private Integer id;

    private String account;

    private String password;

    private String name;

    private Integer type;

    private String avatar;
}
