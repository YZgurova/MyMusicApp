package com.MyMusic.v1.services;

import com.MyMusic.v1.repositories.SongRepository;
import com.MyMusic.v1.repositories.WalletRepository;
import com.MyMusic.v1.services.models.Wallet;

import java.math.BigDecimal;

public class WalletService {
    WalletRepository walletRepository;
    SongRepository songRepository;

    public WalletService(WalletRepository walletRepository, SongRepository songRepository) {
        this.walletRepository = walletRepository;
        this.songRepository=songRepository;
    }

//    public Wallet createWallet(int ownerId) {
//        return Mappers.fromWalletEntity(repository.createWallet(ownerId));
//    }

    public void addMoneyToSongcreator(int songId, BigDecimal amount) {
        int authorId = songRepository.getAutorId(songId);
        addMoney(authorId, amount);
    }

    public void addMoney(int ownerId, BigDecimal amount) {
        walletRepository.addMoney(ownerId, amount);
    }

    public Wallet getWallet(int ownerId) {
        return Mappers.fromWalletEntity(walletRepository.getWallet(ownerId));
    }
}
