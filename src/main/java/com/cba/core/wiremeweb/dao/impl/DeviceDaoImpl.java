package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.DeviceDao;
import com.cba.core.wiremeweb.dto.DeviceRequestDto;
import com.cba.core.wiremeweb.dto.DeviceResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.exception.RecordInUseException;
import com.cba.core.wiremeweb.mapper.DeviceMapper;
import com.cba.core.wiremeweb.model.Device;
import com.cba.core.wiremeweb.model.Status;
import com.cba.core.wiremeweb.repository.DeviceRepository;
import com.cba.core.wiremeweb.repository.UserRepository;
import com.cba.core.wiremeweb.util.UpdateResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor
public class DeviceDaoImpl implements DeviceDao {

    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    @Override
    public Page<DeviceResponseDto> findAll(int page, int pageSize) throws SQLException {

        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Device> DevicesPage = deviceRepository.findAll(pageable);
        if (DevicesPage.isEmpty()) {
            throw new NotFoundException("No Devices found");
        }
        return DevicesPage.map(DeviceMapper::toDto);
    }

    @Override
    public List<DeviceResponseDto> findAll() throws SQLException {

        List<Device> devicesList = deviceRepository.findAll();
        if (devicesList.isEmpty()) {
            throw new NotFoundException("No Devices found");
        }
        return devicesList
                .stream()
                .map(DeviceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DeviceResponseDto> findBySerialNoLike(String serialNumber, int page, int pageSize) throws SQLException {

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Device> DevicesPage = deviceRepository.findBySerialNoLike("%" + serialNumber + "%", pageable);
        if (DevicesPage.isEmpty()) {
            throw new NotFoundException("No Devices found");
        }
        return DevicesPage.map(DeviceMapper::toDto);

    }

    @Override
    public DeviceResponseDto findById(int id) throws SQLException {

        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found"));

        return DeviceMapper.toDto(device);
    }

    @Override
    public DeviceResponseDto deleteById(int id) throws SQLException {
        try {
            Device device = deviceRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Device not found"));

            deviceRepository.deleteById(id);

            return DeviceMapper.toDto(device);

        } catch (NotFoundException nf) {
            throw nf;
        } catch (DataIntegrityViolationException e) {
            throw new RecordInUseException("Device is in use");
        } catch (Exception rr) {
            throw rr;
        }
    }

    @Override
    public void deleteByIdList(List<Integer> deviceList) throws SQLException {
        try {
            for (Integer device : deviceList) {
                Device deviceModel = deviceRepository.findById(device)
                        .orElseThrow(() -> new NotFoundException("Device not found"));
            }

            deviceRepository.deleteAllByIdInBatch(deviceList);

        } catch (NotFoundException nf) {
            throw nf;
        } catch (DataIntegrityViolationException e) {
            throw new RecordInUseException("Device is in use");
        } catch (Exception ee) {
            ee.printStackTrace();
            throw ee;
        }
    }

    @Override
    public UpdateResponse<DeviceResponseDto> updateById(int id, DeviceRequestDto deviceRequestDto) throws SQLException {

        UpdateResponse<DeviceResponseDto> responseBean = new UpdateResponse<>();
        boolean updateRequired = false;
        Map<String, Object> oldDataMap = new HashMap<>();
        Map<String, Object> newDataMap = new HashMap<>();
        Device toBeUpdated = deviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found"));

        if (!toBeUpdated.getDeviceType().equals(deviceRequestDto.getDeviceType())) {
            updateRequired = true;
            oldDataMap.put("deviceType", toBeUpdated.getDeviceType());
            newDataMap.put("deviceType", deviceRequestDto.getDeviceType());

            toBeUpdated.setDeviceType(deviceRequestDto.getDeviceType());
        }
        if (!toBeUpdated.getEmiNo().equals(deviceRequestDto.getEmiNo())) {
            updateRequired = true;
            oldDataMap.put("emiNo", toBeUpdated.getEmiNo());
            newDataMap.put("emiNo", deviceRequestDto.getEmiNo());

            toBeUpdated.setEmiNo(deviceRequestDto.getEmiNo());
        }
        if (!toBeUpdated.getSerialNo().equals(deviceRequestDto.getSerialNo())) {
            updateRequired = true;
            oldDataMap.put("serialNo", toBeUpdated.getSerialNo());
            newDataMap.put("serialNo", deviceRequestDto.getSerialNo());

            toBeUpdated.setSerialNo(deviceRequestDto.getSerialNo());
        }
        if (!toBeUpdated.getStatus().getStatusCode().equals((deviceRequestDto.isActive()) ? "ACTV" : "DACT")) {
            updateRequired = true;
            oldDataMap.put("active", (toBeUpdated.getStatus().getStatusCode().equals("ACTV")) ? true : false);
            newDataMap.put("active", deviceRequestDto.isActive());

            toBeUpdated.setStatus(new Status((deviceRequestDto.isActive()) ? "ACTV" : "DACT"));
        }
        if (updateRequired) {

            deviceRepository.saveAndFlush(toBeUpdated);

            responseBean.setOldDataMap(oldDataMap);
            responseBean.setNewDataMap(newDataMap);
            responseBean.setT(DeviceMapper.toDto(toBeUpdated));

            return responseBean;

        } else {
            throw new NotFoundException("No Changes found");
        }

    }

    @Override
    public DeviceResponseDto create(DeviceRequestDto deviceRequestDto) throws SQLException {
        Device deviceToInsert = DeviceMapper.toModel(deviceRequestDto);
        deviceToInsert.setStatus(new Status(deviceRequestDto.isActive() ? "ACTV" : "DACT"));

        Device savedDevice = deviceRepository.save(deviceToInsert);

        return DeviceMapper.toDto(savedDevice);

    }

    @Override
    public List<DeviceResponseDto> createBulk(List<DeviceRequestDto> deviceRequestDtoList) throws SQLException {

        List<Device> deviceList = new ArrayList<>();
        for (DeviceRequestDto deviceDto : deviceRequestDtoList) {
            Device toBeUpdated = DeviceMapper.toModel(deviceDto);
            deviceList.add(toBeUpdated);
        }
        return deviceRepository.saveAll(deviceList)
                .stream()
                .map(DeviceMapper::toDto)
                .collect(Collectors.toList());
//        return DeviceMapper.toDto(deviceRepository.saveAll(deviceList));
    }

}
