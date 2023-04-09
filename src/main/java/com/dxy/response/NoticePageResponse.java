package com.dxy.response;

import com.dxy.pojo.Notice;
import lombok.Data;

import java.util.List;

@Data
public class NoticePageResponse {
    private List<Notice> notice;

    private Integer totalPage;
}
