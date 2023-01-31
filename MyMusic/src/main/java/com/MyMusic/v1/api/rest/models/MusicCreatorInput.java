package com.MyMusic.v1.api.rest.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MusicCreatorInput {
    public final int userId;

    public MusicCreatorInput(@JsonProperty("userId") int userId) {
        this.userId = userId;
    }
}
