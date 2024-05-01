package com.katerynamykh.taskprofitsoft.backend.repository;

import com.katerynamykh.taskprofitsoft.backend.model.RestorantChain;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RestorantChainRepository extends JpaRepository<RestorantChain, Long>, JpaSpecificationExecutor<RestorantChain> {
    @Override
    @Query(value = "SELECT DISTINCT rch FROM RestorantChain rch LEFT JOIN FETCH rch.branches")
    Page<RestorantChain> findAll(Pageable pageable);

    Optional<RestorantChain> findByNameIgnoreCase(String name);
}
