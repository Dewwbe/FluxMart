package com.cart.controller;

import com.cart.dto.AddToCartRequest;
import com.cart.dto.WishlistRequest;
import com.cart.model.Cart;
import com.cart.model.Wishlist;
import com.cart.service.CartService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@PreAuthorize("hasAnyRole('CUSTOMER','SELLER','SHOP_MANAGER','ADMIN')")
public class CartController {

    private final CartService cartService;

    public CartController(CartService s) { this.cartService = s; }

    // -------- CART --------
    @GetMapping
    public Cart myCart(@RequestHeader("X-USER-ID") Long userId) {
        return cartService.getOrCreateCart(userId);
    }

    @PostMapping("/items")
    public Cart addItem(@RequestHeader("X-USER-ID") Long userId,
                        @Valid @RequestBody AddToCartRequest req) {
        return cartService.addToCart(userId, req.getProductId(), req.getQuantity());
    }

    @PatchMapping("/items/{productId}")
    public Cart updateQty(@RequestHeader("X-USER-ID") Long userId,
                          @PathVariable Long productId,
                          @RequestParam int qty) {
        return cartService.updateQty(userId, productId, qty);
    }

    @DeleteMapping
    public void clear(@RequestHeader("X-USER-ID") Long userId) {
        cartService.clearCart(userId);
    }

    // -------- WISHLIST --------
    @GetMapping("/wishlist")
    public Wishlist myWishlist(@RequestHeader("X-USER-ID") Long userId) {
        return cartService.getOrCreateWishlist(userId);
    }

    @PostMapping("/wishlist/items")
    public Wishlist addToWishlist(@RequestHeader("X-USER-ID") Long userId,
                                  @Valid @RequestBody WishlistRequest req) {
        return cartService.addToWishlist(userId, req.getProductId());
    }

    @DeleteMapping("/wishlist/items/{productId}")
    public Wishlist removeFromWishlist(@RequestHeader("X-USER-ID") Long userId,
                                       @PathVariable Long productId) {
        return cartService.removeFromWishlist(userId, productId);
    }

    @PostMapping("/wishlist/items/{productId}/move-to-cart")
    public Cart moveToCart(@RequestHeader("X-USER-ID") Long userId,
                           @PathVariable Long productId,
                           @RequestParam(defaultValue = "1") int qty) {
        return cartService.moveWishlistItemToCart(userId, productId, qty);
    }
}
