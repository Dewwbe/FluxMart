package com.auth.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "loyalty_wallet")
@Getter @Setter
public class LoyaltyWallet {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Long pointsBalance = 0L;

    @Column(nullable = false)
    private String tier = "BRONZE";

    private Instant updatedAt = Instant.now();

    @PreUpdate
    public void preUpdate() { updatedAt = Instant.now(); }
}
