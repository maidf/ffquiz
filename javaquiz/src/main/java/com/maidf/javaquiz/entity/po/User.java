package com.maidf.javaquiz.entity.po;

import java.io.Serializable;
import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maidf.javaquiz.entity.enums.UserRoleEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("user")
public class User implements Serializable{
    @JsonProperty(required = false)
    @TableId(type = IdType.AUTO)
    private Long id;

    @JsonProperty(required = false)
    private String account;

    @JsonProperty(required = false)
    private String name;

    @JsonIgnore
    @JsonProperty(required = false)
    private String password;

    @JsonProperty(required = false)
    private String email;

    @JsonProperty(required = false)
    private UserRoleEnum role;


    @JsonProperty(value = "create_time", required = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;
}
