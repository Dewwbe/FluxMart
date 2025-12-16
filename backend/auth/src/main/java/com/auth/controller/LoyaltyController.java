package com.auth.controller;

import com.auth.model.LoyaltyLedger;
import com.auth.model.LoyaltyWallet;
import com.auth.repository.LoyaltyLedgerRepository;
import com.auth.service.LoyaltyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/loyalty")
public class LoyaltyController {

    private final LoyaltyService loyaltyService;
    private final LoyaltyLedgerRepository ledgerRepo;

    public LoyaltyController(LoyaltyService s, LoyaltyLedgerRepository l) {
        this.loyaltyService = s;
        this.ledgerRepo = l;
    }

    // CUSTOMER: view balance
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('CUSTOMER','SELLER','SHOP_MANAGER','ADMIN')")
    public LoyaltyWallet myWallet(@RequestHeader("X-USER-ID") Long userId) {
        return loyaltyService.getOrCreateWallet(userId);
    }

    // CUSTOMER: view ledger
    @GetMapping("/me/ledger")
    @PreAuthorize("hasAnyRole('CUSTOMER','SELLER','SHOP_MANAGER','ADMIN')")
    public List<LoyaltyLedger> myLedger(@RequestHeader("X-USER-ID") Long userId) {
        return ledgerRepo.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // INTERNAL: called by Order/Payment service after payment success
    @PostMapping("/earn/order/{orderId}")
    @PreAuthorize("hasAnyRole('ADMIN','SHOP_MANAGER')") // OR secure via internal token
    public LoyaltyWallet earnForOrder(
            @PathVariable String orderId,
            @RequestParam Long userId,
            @RequestParam Long amountLkr
    ) {
        return loyaltyService.earnForOrder(userId, orderId, amountLkr);
    }

    // ADMIN: adjust
    @PostMapping("/admin/adjust")
    @PreAuthorize("hasAnyRole('ADMIN','SHOP_MANAGER')")
    public LoyaltyWallet adminAdjust(
            @RequestParam Long userId,
            @RequestParam Long deltaPoints,
            @RequestParam(required = false) String reason
    ) {
        return loyaltyService.adminAdjust(userId, deltaPoints, reason);
    }
}
