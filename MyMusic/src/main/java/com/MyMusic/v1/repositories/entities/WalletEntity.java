package com.MyMusic.v1.repositories.entities;

import java.math.BigDecimal;

public class WalletEntity {
    public final int ownerId;
    public final BigDecimal resources;

    public WalletEntity(int ownerId, BigDecimal resources) {
        this.ownerId = ownerId;
        this.resources = resources;
    }
}
