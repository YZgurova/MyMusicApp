package com.MyMusic.v1.api.rest.models;

import java.math.BigDecimal;

public class WalletDto {
    public final int ownerId;
    public final BigDecimal resources;

    public WalletDto(int ownerId, BigDecimal resources) {
        this.ownerId = ownerId;
        this.resources = resources;
    }
}
