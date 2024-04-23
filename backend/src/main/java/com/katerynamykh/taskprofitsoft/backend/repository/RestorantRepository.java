package com.katerynamykh.taskprofitsoft.backend.repository;

import com.katerynamykh.taskprofitsoft.backend.model.Restorant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestorantRepository extends JpaRepository<Restorant, Long> {

}
