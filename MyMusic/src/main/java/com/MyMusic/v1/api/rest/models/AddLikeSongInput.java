package com.MyMusic.v1.api.rest.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AddLikeSongInput {
    public final int userId;
    public final int songId;
    public final int votes;

    @JsonCreator
    public AddLikeSongInput(
            @JsonProperty("userId") int userId,
            @JsonProperty("songId") int songId,
            @JsonProperty("votes") int votes) {
        this.userId = userId;
        this.songId = songId;
        this.votes = votes;
    }
}
