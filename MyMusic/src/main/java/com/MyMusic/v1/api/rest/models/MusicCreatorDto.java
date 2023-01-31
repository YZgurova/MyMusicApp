package com.MyMusic.v1.api.rest.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MusicCreatorDto {

    public final String firstName;
    public final String lastName;
    public final String username;
    public final String email;

    @JsonCreator
    public MusicCreatorDto(
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("username") String username,
            @JsonProperty("email") String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
    }
}
