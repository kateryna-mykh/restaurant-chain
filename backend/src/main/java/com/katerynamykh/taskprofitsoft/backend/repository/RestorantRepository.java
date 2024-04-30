package com.katerynamykh.taskprofitsoft.backend.repository;

import com.katerynamykh.taskprofitsoft.backend.model.Restorant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RestorantRepository extends JpaRepository<Restorant, Long> {
    Optional<Restorant> findByLocationAddressIgnoreCase(String address);

    @EntityGraph(attributePaths = { "restorantChain" })
    @Query("SELECT r FROM Restorant r "
            + "WHERE (coalesce(?1) IS NULL OR (LOWER(r.locationAddress) LIKE LOWER(%?1%))) "
            + "AND (coalesce(?2) IS NULL OR (r.restorantChain.id = ?2))")
    Page<Restorant> findAllByParamsIgnoreCase(String address, Long chainId, Pageable pageable);

    @EntityGraph(attributePaths = { "restorantChain" })
    @Query("SELECT r FROM Restorant r "
            + "WHERE (coalesce(?1) IS NULL OR (LOWER(r.locationAddress) LIKE LOWER(%?1%))) "
            + "AND (coalesce(?2) IS NULL OR (r.restorantChain.id = ?2))")
    List<Restorant> findAllByParamsIgnoreCase(String address, Long chainId);
}
