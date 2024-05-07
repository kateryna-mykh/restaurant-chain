package com.katerynamykh.taskprofitsoft.backend.repository;

import com.katerynamykh.taskprofitsoft.backend.model.Restaurant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RestaurantRepository
        extends JpaRepository<Restaurant, Long>, JpaSpecificationExecutor<Restaurant> {
    Optional<Restaurant> findByLocationAddressIgnoreCase(String address);
}
