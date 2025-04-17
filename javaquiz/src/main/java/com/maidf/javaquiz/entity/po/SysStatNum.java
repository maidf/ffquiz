package com.maidf.javaquiz.entity.po;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysStatNum implements Serializable {
    private Long user;
    private Long bank;
    private Long qn;
    private Long paper;
    @JsonProperty("today_use")
    private Long todayUseNum;
    @JsonProperty("today_use_user")
    private Long todayUseUser;
    @JsonProperty("yesday_use")
    private Long yesdayUseNum;
    @JsonProperty("yesday_use_user")
    private Long yesdayUseUser;
}
