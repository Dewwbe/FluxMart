package com.catalog.controller;

import com.catalog.dto.CategoryRequest;
import com.catalog.model.Category;
import com.catalog.repository.CategoryRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/categories")
@PreAuthorize("hasAnyRole('ADMIN','SHOP_MANAGER')")
public class CategoryController {

    private final CategoryRepository repo;

    public CategoryController(CategoryRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Category create(@RequestBody CategoryRequest req) {
        Category c = new Category();
        c.setName(req.getName());
        return repo.save(c);
    }

    @GetMapping
    public List<Category> list() {
        return repo.findAll();
    }
}

