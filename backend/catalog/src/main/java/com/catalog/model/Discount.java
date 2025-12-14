package com.catalog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "discounts")
@Getter @Setter
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private DiscountType type; // PERCENTAGE or FIXED

    private BigDecimal value;

    private LocalDateTime startAt;
    private LocalDateTime endAt;

    private boolean active;
}

