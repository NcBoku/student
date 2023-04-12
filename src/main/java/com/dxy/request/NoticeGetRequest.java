package com.dxy.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeGetRequest {
    private Integer page;
    private Integer size;
    private String keyword;
}
