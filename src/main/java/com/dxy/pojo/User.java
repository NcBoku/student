package com.dxy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @TableId(type= IdType.AUTO)
    private Integer id;

    private String account;

    private String password;

    private String name;

    private Integer type;

    private String avatar;
}
