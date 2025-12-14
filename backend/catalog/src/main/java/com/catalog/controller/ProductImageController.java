package com.catalog.controller;

import com.catalog.model.Product;
import com.catalog.model.ProductImage;
import com.catalog.repository.ProductImageRepository;
import com.catalog.repository.ProductRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@RestController
@RequestMapping("/api/catalog/products/{productId}/images")
public class ProductImageController {

    private final ProductRepository productRepo;
    private final ProductImageRepository imageRepo;

    public ProductImageController(ProductRepository p, ProductImageRepository i) {
        this.productRepo = p;
        this.imageRepo = i;
    }

    // PUBLIC
    @GetMapping
    public List<ProductImage> list(@PathVariable Long productId) {
        return imageRepo.findByProductId(productId);
    }

    // SELLER / ADMIN
    @PostMapping
    @PreAuthorize("hasAnyRole('SELLER','ADMIN','SHOP_MANAGER')")
    public ProductImage upload(
            @PathVariable Long productId,
            @RequestBody ProductImage image
    ) {
        Product product = productRepo.findById(productId).orElseThrow();
        image.setProduct(product);
        return imageRepo.save(image);
    }
}

