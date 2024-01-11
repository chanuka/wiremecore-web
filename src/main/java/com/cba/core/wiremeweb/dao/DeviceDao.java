package com.cba.core.wiremeweb.dao;

import java.util.List;

public interface DeviceDao<T, K> extends GenericDao<T, K> {

    List<Object[]> getDeviceDistribution(String selectClause, String groupByClause) throws Exception;

    List<T> getGeoFenceDevice(Boolean isAway) throws Exception;
}
