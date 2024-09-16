package com.example.eztask.enums;


import lombok.Getter;

@Getter
public enum ResponseCode {

    SUCCESS("200", "Success"),
    BAD_REQUEST("400", "Bad Request"),
    NOT_FOUND("404", "Not Found"),
    SERVER_ERROR("500", "Internal Server Error"),
    UNAVAILABLE("503", "Service Unavailable"),
    INVALID_PARAMETER("400", "Invalid Parameter");

    private final String code;
    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
