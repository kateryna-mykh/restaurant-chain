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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "restaurant_chains")
public class RestaurantChain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private String cuisine;
    @Column(columnDefinition = "decimal(10,2) dafault 0.00", nullable = false)
    private BigDecimal annualRevenue;
    @OneToMany(mappedBy = "restaurantChain", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Restaurant> branches = new HashSet<>();
}