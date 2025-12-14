package com.catalog.dto;

import com.catalog.model.DiscountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DiscountRequest {

    private String name;
    private DiscountType type;   // PERCENTAGE or FIXED
    private BigDecimal value;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DiscountType getType() {
        return type;
    }

    public void setType(DiscountType type) {
        this.type = type;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public void setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }
}
