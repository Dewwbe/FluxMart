package com.catalog.service;

import com.catalog.dto.ProductRequest;
import com.catalog.model.Category;
import com.catalog.model.Product;
import com.catalog.repository.CategoryRepository;
import com.catalog.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    public ProductService(ProductRepository p, CategoryRepository c) {
        this.productRepo = p;
        this.categoryRepo = c;
    }

    public Product createProduct(ProductRequest req, Long sellerId) {
        Product p = new Product();
        p.setName(req.getName());
        p.setDescription(req.getDescription());
        p.setPrice(req.getPrice());
        p.setCurrency("LKR");
        p.setSellerId(sellerId);

        Set<Category> categories = new HashSet<>(
                categoryRepo.findAllById(req.getCategoryIds())
        );
        p.setCategories(categories);

        return productRepo.save(p);
    }

    public List<Product> getActiveProducts() {
        return productRepo.findByActiveTrueAndApprovedTrue();
    }

    public Product approveProduct(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setApproved(true);
        return productRepo.save(product);
    }
}

