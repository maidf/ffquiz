package com.maidf.javaquiz.entity.po;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Component
@Data
public class BankQnNum implements Serializable {
    private Long id;
    private String name;
    private String sub;
    @JsonProperty("qn_nums")
    private Long qnNums;
    @JsonProperty("use_nums")
    private Long useNums;
    @JsonProperty("err_nums")
    private Long errNums;
}
