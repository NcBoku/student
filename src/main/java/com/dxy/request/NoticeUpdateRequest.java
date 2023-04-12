package com.dxy.request;

import lombok.Data;

import java.util.Date;

@Data
public class NoticeUpdateRequest {
    private Integer id;

    private String title;

    private String content;

    private String sender;

    private Integer type;
}
