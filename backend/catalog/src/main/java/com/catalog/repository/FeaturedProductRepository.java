package com.catalog.repository;


import com.catalog.model.FeaturedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeaturedProductRepository extends JpaRepository<FeaturedProduct, Long> {}
