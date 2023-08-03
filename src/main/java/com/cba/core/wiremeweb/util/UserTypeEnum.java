package com.cba.core.wiremeweb.util;

public enum UserTypeEnum {
    WEB(1),
    MPOS(2);

    private final int value;

    UserTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
