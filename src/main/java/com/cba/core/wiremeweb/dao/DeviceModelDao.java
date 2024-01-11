package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.model.DeviceModel;

import java.util.List;

public interface DeviceModelDao<T, K> extends GenericDao<T, K> {
    public List<DeviceModel> findAllByDeviceVendor_Id(int VendorId) throws Exception;
}
