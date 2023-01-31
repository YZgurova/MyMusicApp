package com.MyMusic.v1.services;

import com.MyMusic.v1.repositories.entities.*;
import com.MyMusic.v1.services.models.*;

public class Mappers {
    public static User fromUserEntity(UserEntity user) {
        return new User(user.id,user.firstName, user.lastName, user.username, user.email);
    }

    public static MusicCreator fromMusicCreatorEntity(MusicCreatorEntity creatorEntity) {
        return new MusicCreator(
                new User(creatorEntity.creatorData.id, creatorEntity.creatorData.firstName, creatorEntity.creatorData.lastName,
                        creatorEntity.creatorData.username, creatorEntity.creatorData.email));
    }

    public static Song fromSongEntity(SongEntity songEntity) {
        return new Song(songEntity.id, songEntity.authorUsername,songEntity.name, songEntity.url, songEntity.price, songEntity.votes, songEntity.isLoved(), songEntity.isBought());
    }

    public static Credentials fromCredentialsEntity(CredentialsEntity credentialsEntity) {
        return new Credentials(credentialsEntity.id, credentialsEntity.username, credentialsEntity.roles);
    }

    public static Transaction fromTransactionEntity(TransactionEntity transactionEntity) {
        return new Transaction(transactionEntity.id, transactionEntity.userId, transactionEntity.transactionDescription, transactionEntity.Status);
    }
    public static Wallet fromWalletEntity(WalletEntity walletEntity) {
        return new Wallet(walletEntity.ownerId, walletEntity.resources);
    }
}
