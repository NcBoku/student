package com.dxy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @TableId(type= IdType.AUTO)
    private Integer id;

    private String number;

    private String name;

    private String sex;

    private String phone;

    private String qq;

    private String photo;

    private Integer clazzId;

    private Integer graderId;
}
