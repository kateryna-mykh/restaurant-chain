package com.katerynamykh.taskprofItsoft.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name = "restorants")
public class Restorant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String locationAddress;
    @NotNull
    private String manager;
    @Column(nullable = false)
    private Integer seetsCapacity;
    @Column(nullable = false)
    private Integer employeesNumber;
    @Column(
        name = "menu_items",
        columnDefinition = "text"
    )
    private List<String> menuItems;
    @ManyToOne
    @JoinColumn(name = "chain_id", nullable = false)
    private RestorantChain restorantChain;
}
