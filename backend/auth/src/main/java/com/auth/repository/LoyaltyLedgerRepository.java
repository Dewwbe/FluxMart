package com.auth.repository;

import com.auth.model.LoyaltyLedger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoyaltyLedgerRepository extends JpaRepository<LoyaltyLedger, Long> {
    List<LoyaltyLedger> findByUserIdOrderByCreatedAtDesc(Long userId);
    boolean existsByUserIdAndReasonAndReferenceId(Long userId, String reason, String referenceId);
}
