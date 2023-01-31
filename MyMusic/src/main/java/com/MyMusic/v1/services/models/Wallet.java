package com.MyMusic.v1.services.models;

import java.math.BigDecimal;

public class Wallet {
    public final int ownerId;
    public final BigDecimal resources;

    public Wallet(int ownerId, BigDecimal resources) {
        this.ownerId = ownerId;
        this.resources = resources;
    }
}
