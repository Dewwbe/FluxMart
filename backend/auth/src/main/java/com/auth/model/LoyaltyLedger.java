package com.auth.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "loyalty_ledger")
@Getter @Setter
public class LoyaltyLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(nullable = false)
    private String type; // EARN, REDEEM, ADJUST

    @Column(nullable = false)
    private Long points; // + or -

    @Column(nullable = false)
    private String reason; // ORDER_PAID, ADMIN_ADJUST, etc.

    private String referenceType; // ORDER, ADMIN, PROMO
    private String referenceId;   // orderId

    private Instant createdAt = Instant.now();
}
