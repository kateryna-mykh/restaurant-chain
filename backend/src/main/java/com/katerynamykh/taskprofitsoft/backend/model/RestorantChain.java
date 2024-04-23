package com.katerynamykh.taskprofitsoft.backend.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name = "restorant_chains")
public class RestorantChain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private String cuisine;
    @Column(columnDefinition = "decimal(10,2) dafault 0.00", nullable = false)
    private BigDecimal annualRevenue;
    @OneToMany(mappedBy = "restorantChain", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Restorant> branches = new HashSet<>();
}
