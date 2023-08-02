package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.DeviceDao;
import com.cba.core.wiremeweb.dto.DeviceRequestDto;
import com.cba.core.wiremeweb.dto.DeviceResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.exception.RecordInUseException;
import com.cba.core.wiremeweb.mapper.DeviceMapper;
import com.cba.core.wiremeweb.model.Device;
import com.cba.core.wiremeweb.model.Status;
import com.cba.core.wiremeweb.model.User;
import com.cba.core.wiremeweb.repository.UserRepository;
import com.cba.core.wiremeweb.util.UserBean;
import com.cba.core.wiremeweb.repository.DeviceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@Transactional
@RequiredArgsConstructor
public class DeviceDaoImpl implements DeviceDao {

    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;
    private final UserBean userBean;

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
    public void deleteById(int id) throws SQLException {
        try {
            Device device = deviceRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Device not found"));

            deviceRepository.deleteById(id);
        } catch (NotFoundException nf) {
            throw nf;
        } catch (DataIntegrityViolationException e) {
            throw new RecordInUseException("Device is in use");
        } catch (Exception rr) {
            throw rr;
        }
    }

    @Override
    public void deleteByIdList(List<Map<String, Integer>> deviceList) throws SQLException {
        try {
            for (Map<String, Integer> device : deviceList) {
                Device deviceModel = deviceRepository.findById(device.get("id"))
                        .orElseThrow(() -> new NotFoundException("Device not found"));

                deviceRepository.deleteById(device.get("id"));
            }
        } catch (NotFoundException nf) {
            throw nf;
        } catch (DataIntegrityViolationException e) {
            throw new RecordInUseException("Device is in use");
        } catch (Exception ee) {
            throw ee;
        }
    }

    @Override
    public DeviceResponseDto updateById(int id, DeviceRequestDto deviceRequestDto) throws SQLException {

        Device toBeUpdated = deviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Device not found"));

        toBeUpdated.setDeviceType(deviceRequestDto.getDeviceType());
        toBeUpdated.setEmiNo(deviceRequestDto.getEmiNo());
        toBeUpdated.setSerialNo(deviceRequestDto.getSerialNo());
        User usr = userRepository.findByUserName(userBean.getUsername());
        toBeUpdated.setUserByModifiedBy(usr);
        toBeUpdated.setUpdatedAt(new Date());

        deviceRepository.save(toBeUpdated);

        return DeviceMapper.toDto(toBeUpdated);

    }

    @Override
    public DeviceResponseDto create(DeviceRequestDto deviceRequestDto) throws SQLException {

        Device deviceToInsert = DeviceMapper.toModel(deviceRequestDto);

        User usr = userRepository.findByUserName(userBean.getUsername());
        deviceToInsert.setUserByCreatedBy(usr);
        deviceToInsert.setUserByModifiedBy(usr);
        deviceToInsert.setCreatedAt(new Date());
        deviceToInsert.setUpdatedAt(new Date());
        deviceToInsert.setStatus(new Status(deviceRequestDto.isActive() ? "ACTV" : "DACT"));

        Device savedDevice = deviceRepository.save(deviceToInsert);

        return DeviceMapper.toDto(savedDevice);

    }

    @Override
    public void createBulk(List<DeviceRequestDto> deviceRequestDtoList) throws SQLException {

        for (DeviceRequestDto deviceDto : deviceRequestDtoList) {
            Device toBeUpdated = DeviceMapper.toModel(deviceDto);

            User usr = userRepository.findByUserName(userBean.getUsername());
            toBeUpdated.setUserByCreatedBy(usr);
            toBeUpdated.setUserByModifiedBy(usr);
            toBeUpdated.setCreatedAt(new Date());
            toBeUpdated.setUpdatedAt(new Date());
//                toBeUpdated.setStatus(new Status(deviceDto.isActive() ? "ACTV" : "DACT"));
            toBeUpdated.setStatus(new Status("ACTV"));
            deviceRepository.save(toBeUpdated);

        }
    }

}
