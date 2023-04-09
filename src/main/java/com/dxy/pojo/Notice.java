package com.dxy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notice {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String title;

    private String content;

    private String sender;

    private Date time;

    private Integer type;
}
