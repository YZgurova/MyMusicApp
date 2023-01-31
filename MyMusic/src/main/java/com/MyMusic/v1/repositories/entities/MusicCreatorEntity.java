package com.MyMusic.v1.repositories.entities;

public class MusicCreatorEntity {
    public final UserEntity creatorData;

    public MusicCreatorEntity(UserEntity creatorData) {
        this.creatorData=creatorData;
    }
}
