package com.hoanhtuan.main_app_security.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCodeMessage {
    SUCCESS("000", "Success"),

    ;
    private final String code;
    private final String message;
}
