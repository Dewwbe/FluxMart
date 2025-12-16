package com.cart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class WishlistRequest {
    @NotNull
    private Long productId;
}
