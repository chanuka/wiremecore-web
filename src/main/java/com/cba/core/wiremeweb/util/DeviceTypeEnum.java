package com.cba.core.wiremeweb.util;

public enum DeviceTypeEnum {
    WEB(1),
    MPOS(2);

    private final int value;

    DeviceTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
