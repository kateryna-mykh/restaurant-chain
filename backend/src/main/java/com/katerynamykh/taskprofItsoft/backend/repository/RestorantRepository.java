package com.katerynamykh.taskprofItsoft.backend.repository;

import com.katerynamykh.taskprofItsoft.backend.model.Restorant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestorantRepository extends JpaRepository<Restorant, Long> {

}
