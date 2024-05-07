package com.katerynamykh.taskprofitsoft.backend.repository;

import com.katerynamykh.taskprofitsoft.backend.model.RestaurantChain;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface RestaurantChainRepository extends JpaRepository<RestaurantChain, Long>, JpaSpecificationExecutor<RestaurantChain> {
    @Override
    @Query(value = "SELECT DISTINCT rch FROM RestaurantChain rch LEFT JOIN FETCH rch.branches")
    Page<RestaurantChain> findAll(Pageable pageable);

    Optional<RestaurantChain> findByNameIgnoreCase(String name);
}
