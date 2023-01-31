package com.MyMusic.v1.repositories;

import com.MyMusic.v1.repositories.entities.WalletEntity;

import java.math.BigDecimal;

public interface WalletRepository {
    WalletEntity createWallet(int ownerId);
    void addMoney(int ownerId, BigDecimal amount);
    WalletEntity getWallet(int ownerId);
}
