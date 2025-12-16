package com.auth.service;

import com.auth.model.LoyaltyLedger;
import com.auth.model.LoyaltyWallet;
import com.auth.repository.LoyaltyLedgerRepository;
import com.auth.repository.LoyaltyWalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class LoyaltyService {

    private final LoyaltyWalletRepository walletRepo;
    private final LoyaltyLedgerRepository ledgerRepo;

    public LoyaltyService(LoyaltyWalletRepository w, LoyaltyLedgerRepository l) {
        this.walletRepo = w;
        this.ledgerRepo = l;
    }

    public LoyaltyWallet getOrCreateWallet(Long userId) {
        return walletRepo.findById(userId).orElseGet(() -> {
            LoyaltyWallet w = new LoyaltyWallet();
            w.setUserId(userId);
            w.setPointsBalance(0L);
            w.setTier("BRONZE");
            return walletRepo.save(w);
        });
    }

    /** Example policy: 1 point per 100 LKR spent (tweak as you like) */
    public long calculatePointsFromAmount(long amountLkr) {
        return Math.max(0, amountLkr / 100);
    }

    public LoyaltyWallet earnForOrder(Long userId, String orderId, long amountLkr) {
        // idempotency: if already earned for this order, return current wallet
        if (ledgerRepo.existsByUserIdAndReasonAndReferenceId(userId, "ORDER_PAID", orderId)) {
            return getOrCreateWallet(userId);
        }

        long points = calculatePointsFromAmount(amountLkr);
        LoyaltyWallet wallet = getOrCreateWallet(userId);
        wallet.setPointsBalance(wallet.getPointsBalance() + points);

        LoyaltyLedger entry = new LoyaltyLedger();
        entry.setUserId(userId);
        entry.setType("EARN");
        entry.setPoints(points);
        entry.setReason("ORDER_PAID");
        entry.setReferenceType("ORDER");
        entry.setReferenceId(orderId);

        ledgerRepo.save(entry);
        return walletRepo.save(wallet);
    }

    public LoyaltyWallet redeem(Long userId, long points, String referenceId) {
        if (points <= 0) throw new IllegalArgumentException("Points must be > 0");
        LoyaltyWallet wallet = getOrCreateWallet(userId);
        if (wallet.getPointsBalance() < points) throw new IllegalArgumentException("Insufficient points");

        wallet.setPointsBalance(wallet.getPointsBalance() - points);

        LoyaltyLedger entry = new LoyaltyLedger();
        entry.setUserId(userId);
        entry.setType("REDEEM");
        entry.setPoints(-points);
        entry.setReason("REDEEM_AT_CHECKOUT");
        entry.setReferenceType("ORDER");
        entry.setReferenceId(referenceId);

        ledgerRepo.save(entry);
        return walletRepo.save(wallet);
    }

    public LoyaltyWallet adminAdjust(Long userId, long deltaPoints, String reason) {
        LoyaltyWallet wallet = getOrCreateWallet(userId);
        wallet.setPointsBalance(wallet.getPointsBalance() + deltaPoints);

        LoyaltyLedger entry = new LoyaltyLedger();
        entry.setUserId(userId);
        entry.setType("ADJUST");
        entry.setPoints(deltaPoints);
        entry.setReason(reason == null ? "ADMIN_ADJUST" : reason);
        entry.setReferenceType("ADMIN");
        entry.setReferenceId("manual");

        ledgerRepo.save(entry);
        return walletRepo.save(wallet);
    }
}
