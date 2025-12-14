package com.catalog.controller;

import com.catalog.dto.ProductRequest;
import com.catalog.model.Product;
import com.catalog.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService ps) {
        this.productService = ps;
    }

    // BUYER
    @GetMapping
    public List<Product> listProducts() {
        return productService.getActiveProducts();
    }

    // SELLER
    @PostMapping
    @PreAuthorize("hasAnyRole('SELLER','ADMIN','SHOP_MANAGER')")
    public Product createProduct(
            @RequestBody ProductRequest request,
            @RequestHeader("X-USER-ID") Long sellerId
    ) {
        return productService.createProduct(request, sellerId);
    }

    @PatchMapping("/{productId}/approve")
    @PreAuthorize("hasAnyRole('ADMIN','SHOP_MANAGER')")
    public Product approveProduct(@PathVariable Long productId) {
        return productService.approveProduct(productId);
    }

}
