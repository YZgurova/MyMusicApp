package com.MyMusic.v1.api.rest;

import com.MyMusic.v1.api.rest.models.*;
import com.MyMusic.v1.services.models.*;

public class Mappers {
    public static UserDto fromUser(User user) {
        return new UserDto(user.id, user.firstName, user.lastName, user.username, user.email);
    }

    public static SongDto fromSong(Song song) {
        return new SongDto(song.id, song.name, song.url, song.author, song.price, song.votes, song.isLoved(), song.isBought());
    }

    public static MusicCreatorDto fromCreator(MusicCreator creator) {
        return new MusicCreatorDto(creator.userProperties.firstName, creator.userProperties.lastName, creator.userProperties.username, creator.userProperties.email);
    }

    public static WalletDto fromWallet(Wallet wallet) {
        return new WalletDto(wallet.ownerId, wallet.resources);
    }

    public static TransactionDto fromTransaction(Transaction transaction) {
        return new TransactionDto(transaction.id, transaction.userId, transaction.transactionDescription, transaction.Status);
    }


}
