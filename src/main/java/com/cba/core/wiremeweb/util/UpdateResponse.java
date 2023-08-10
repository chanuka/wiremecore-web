package com.cba.core.wiremeweb.util;

import lombok.Data;

import java.util.Map;

@Data
public class UpdateResponse<T> {

    private T t;
    private Map<String, Object> oldDataMap;
    private Map<String, Object> newDataMap;
}
