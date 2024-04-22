package com.katerynamykh.taskprofItsoft.backend.repository;

import com.katerynamykh.taskprofItsoft.backend.model.RestorantChain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestorantChainRepository extends JpaRepository<RestorantChain, Long> {

}
