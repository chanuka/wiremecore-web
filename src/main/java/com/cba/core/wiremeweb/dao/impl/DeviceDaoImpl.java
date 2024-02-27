package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.DeviceDao;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.model.Device;
import com.cba.core.wiremeweb.repository.DeviceRepository;
import com.cba.core.wiremeweb.repository.specification.DeviceSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class DeviceDaoImpl implements DeviceDao<Device> {

    private final DeviceRepository repository;
    @PersistenceContext
    private EntityManager entityManager;


    @Override
//    @Cacheable("devices")
    public Page<Device> findAll(int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        return repository.findAll(pageable);
    }

    @Override
//    @Cacheable("devices")
    public List<Device> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Page<Device> findBySearchParamLike(Map<String, String> searchParamList, int page, int pageSize) throws Exception {

        Pageable pageable = PageRequest.of(page, pageSize);
        Specification<Device> spec = DeviceSpecification.serialNoLikeAndDeviceTypeLike(
                searchParamList.get("serialNumber"),
                searchParamList.get("deviceType"),
                searchParamList.get("status"),
                searchParamList.get("deviceModel"),
                searchParamList.get("deviceVendor")
        );

        return repository.findAll(spec, pageable);

    }

    @Override
    public List<Device> findBySearchParamLikeByKeyWord(Map<String, String> searchParameter) throws Exception {
        Specification<Device> spec = DeviceSpecification.allLike(searchParameter.get("keyWord"));
        return repository.findAll(spec);
    }

    @Override
    public Device findById(int id) throws Exception {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Device not found"));
    }

    @Override
//    @CacheEvict(value = "devices", allEntries = true)
    public void deleteById(int id) throws Exception {
        repository.deleteById(id);
    }

    @Override
//    @CacheEvict(value = "devices", allEntries = true)
    public void deleteByIdList(List<Integer> idList) throws Exception {
        repository.deleteAllByIdInBatch(idList);
    }

    @Override
//    @CacheEvict(value = "devices", allEntries = true)
    public Device updateById(int id, Device device) throws Exception {
        return repository.saveAndFlush(device);
    }

    @Override
//    @CacheEvict(value = "devices", allEntries = true)
    public Device create(Device device) throws Exception {
        return repository.save(device);
    }

    @Override
//    @CacheEvict(value = "devices", allEntries = true)
    public List<Device> createBulk(List<Device> entityList) throws Exception {
        return repository.saveAll(entityList);
    }

    @Override
    public List<Object[]> getDeviceDistribution(String selectClause, String groupByClause) throws Exception {

        String jpql = "SELECT " + selectClause + " from Device d inner join DeviceModel dm ON d.deviceModel.id = dm.id inner join DeviceVendor dv on dm.deviceVendor.id = dv.id " +
                " GROUP BY " + groupByClause;

        Query query = entityManager.createQuery(jpql);
        return query.getResultList();

    }

    @Override
    public List<Device> getGeoFenceDevice(Boolean isAway) throws Exception {

        List<Device> entityList = repository.findByIsAway(isAway);
        if (entityList.isEmpty()) {
            throw new NotFoundException("No Devices found");
        }

        return entityList;

    }

}
