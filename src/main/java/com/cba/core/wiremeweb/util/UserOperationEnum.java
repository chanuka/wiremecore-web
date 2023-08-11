package com.cba.core.wiremeweb.util;

public enum UserOperationEnum {

    READ("READ"),
    SEARCH("SEARCH"),
    CREATE("CREATE"),
    UPDATE("UPDATE"),
    DELETE("DELETE");

    private final String value;

    UserOperationEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
