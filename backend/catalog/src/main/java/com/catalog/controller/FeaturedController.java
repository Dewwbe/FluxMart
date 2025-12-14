package com.catalog.controller;

import com.catalog.model.FeaturedProduct;
import com.catalog.model.Product;
import com.catalog.repository.FeaturedProductRepository;
import com.catalog.repository.ProductRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/catalog/featured")
@PreAuthorize("hasAnyRole('ADMIN','SHOP_MANAGER')")
public class FeaturedController {

    private final FeaturedProductRepository repo;
    private final ProductRepository productRepo;

    public FeaturedController(FeaturedProductRepository r, ProductRepository p) {
        this.repo = r;
        this.productRepo = p;
    }

    @PostMapping("/{productId}")
    public FeaturedProduct feature(@PathVariable Long productId) {
        Product p = productRepo.findById(productId).orElseThrow();

        FeaturedProduct f = new FeaturedProduct();
        f.setProduct(p);
        f.setDisplayOrder(1);

        return repo.save(f);
    }
}

