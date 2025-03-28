package com.maidf.javaquiz.util;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maidf.javaquiz.entity.enums.CodeEnum;

@Component
public class Result {

    public static ResponseEntity<String> success() {
        CodeEnum c = CodeEnum.SUCCESS;
        return ResponseEntity.status(c.code()).body(c.msg());
    }

    public static ResponseEntity<String> success(Object data) {
        try {
            CodeEnum c = CodeEnum.SUCCESS;
            return ResponseEntity.status(c.code()).body(new ObjectMapper().writeValueAsString(data));
        } catch (JsonProcessingException e) {
            CodeEnum c = CodeEnum.EXCEPTION;
            return ResponseEntity.status(c.code()).body(c.msg());
        }
    }

    public static ResponseEntity<String> error() {
        CodeEnum c = CodeEnum.ERROR;

        return ResponseEntity.status(c.code()).body(c.msg());
    }

    public static ResponseEntity<String> error(String msg) {
        CodeEnum c = CodeEnum.ERROR;

        return ResponseEntity.status(c.code()).body(msg);
    }

    public static ResponseEntity<String> exception() {
        CodeEnum c = CodeEnum.EXCEPTION;

        return ResponseEntity.status(c.code()).body(c.msg());
    }
}