package com.dxy.request;

import lombok.Data;

@Data
public class NoticeInsertRequest {
    private String title;

    private String content;

    private String sender;

    private Integer type;
}
