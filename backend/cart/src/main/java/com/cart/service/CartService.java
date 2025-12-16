package com.cart.service;

import com.cart.model.*;
import com.cart.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;
    private final WishlistRepository wishlistRepo;
    private final WishlistItemRepository wishlistItemRepo;

    public CartService(CartRepository c, CartItemRepository ci,
                       WishlistRepository w, WishlistItemRepository wi) {
        this.cartRepo = c;
        this.cartItemRepo = ci;
        this.wishlistRepo = w;
        this.wishlistItemRepo = wi;
    }

    // ---------- CART ----------
    public Cart getOrCreateCart(Long userId) {
        return cartRepo.findByUserId(userId).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUserId(userId);
            return cartRepo.save(cart);
        });
    }

    public Cart addToCart(Long userId, Long productId, int qty) {
        Cart cart = getOrCreateCart(userId);

        CartItem item = cartItemRepo.findByCartIdAndProductId(cart.getId(), productId)
                .orElseGet(() -> {
                    CartItem ci = new CartItem();
                    ci.setCart(cart);
                    ci.setProductId(productId);
                    ci.setQuantity(0);
                    return ci;
                });

        item.setQuantity(item.getQuantity() + qty);
        cartItemRepo.save(item);
        return getOrCreateCart(userId); // reload style
    }

    public Cart updateQty(Long userId, Long productId, int qty) {
        Cart cart = getOrCreateCart(userId);
        CartItem item = cartItemRepo.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (qty <= 0) {
            cartItemRepo.delete(item);
        } else {
            item.setQuantity(qty);
            cartItemRepo.save(item);
        }
        return getOrCreateCart(userId);
    }

    public void clearCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().clear();
        cartRepo.save(cart);
    }

    // ---------- WISHLIST ----------
    public Wishlist getOrCreateWishlist(Long userId) {
        return wishlistRepo.findByUserId(userId).orElseGet(() -> {
            Wishlist w = new Wishlist();
            w.setUserId(userId);
            return wishlistRepo.save(w);
        });
    }

    public Wishlist addToWishlist(Long userId, Long productId) {
        Wishlist w = getOrCreateWishlist(userId);

        wishlistItemRepo.findByWishlistIdAndProductId(w.getId(), productId)
                .orElseGet(() -> {
                    WishlistItem wi = new WishlistItem();
                    wi.setWishlist(w);
                    wi.setProductId(productId);
                    return wishlistItemRepo.save(wi);
                });

        return getOrCreateWishlist(userId);
    }

    public Wishlist removeFromWishlist(Long userId, Long productId) {
        Wishlist w = getOrCreateWishlist(userId);
        WishlistItem wi = wishlistItemRepo.findByWishlistIdAndProductId(w.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Wishlist item not found"));
        wishlistItemRepo.delete(wi);
        return getOrCreateWishlist(userId);
    }

    /** Move a wishlist product into cart (common ecommerce UX) */
    public Cart moveWishlistItemToCart(Long userId, Long productId, int qty) {
        // remove from wishlist if exists
        Wishlist w = getOrCreateWishlist(userId);
        wishlistItemRepo.findByWishlistIdAndProductId(w.getId(), productId)
                .ifPresent(wishlistItemRepo::delete);

        // add to cart
        return addToCart(userId, productId, qty);
    }
}
