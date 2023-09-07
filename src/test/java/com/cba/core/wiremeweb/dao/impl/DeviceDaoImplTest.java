package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dto.DeviceRequestDto;
import com.cba.core.wiremeweb.dto.DeviceResponseDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@SpringBootTest
//@Sql(scripts = {"/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class DeviceDaoImplTest {

    @Autowired
    DeviceDaoImpl deviceDaoImpl;

//    @Test
//    @Transactional
//    @Rollback(value = true)
//    public void testCreate() throws Exception {
//        DeviceRequestDto deviceRequestDto = new DeviceRequestDto("87878787", "4555555", "POS", "ACTV");
//        deviceDaoImpl.create(deviceRequestDto);
//
//        List<DeviceResponseDto> allList = deviceDaoImpl.findAll();
//        Assertions.assertEquals(1, allList.size());
//    }

}