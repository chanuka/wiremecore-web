package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.UserConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface DashBoardRepository extends JpaRepository<UserConfig, Integer>, JpaSpecificationExecutor<UserConfig> {

    List<UserConfig> findByUser_NameAndConfigType(String userName, String configType);

    Optional<UserConfig> findByUser_NameAndConfigName(String userName, String configName);

    void deleteByUser_NameAndConfigName(String userName, String configName);

}
