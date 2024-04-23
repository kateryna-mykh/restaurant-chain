package com.katerynamykh.taskprofitsoft.backend.repository;

import com.katerynamykh.taskprofitsoft.backend.model.RestorantChain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestorantChainRepository extends JpaRepository<RestorantChain, Long> {

}
