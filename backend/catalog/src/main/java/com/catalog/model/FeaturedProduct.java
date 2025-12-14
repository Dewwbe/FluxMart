package com.catalog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "featured_products")
@Getter @Setter
public class FeaturedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    private LocalDateTime startAt;
    private LocalDateTime endAt;

    private int displayOrder;

    // getters & setters
}
