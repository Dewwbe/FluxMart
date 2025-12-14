package com.catalog.controller;

import com.catalog.dto.DiscountRequest;
import com.catalog.model.Discount;
import com.catalog.repository.DiscountRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/catalog/discounts")
@PreAuthorize("hasAnyRole('ADMIN','SHOP_MANAGER')")
public class DiscountController {

    private final DiscountRepository repo;

    public DiscountController(DiscountRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Discount create(@RequestBody DiscountRequest req) {
        Discount d = new Discount();
        d.setName(req.getName());
        d.setType(req.getType());
        d.setValue(req.getValue());
        d.setActive(true);
        return repo.save(d);
    }
}

