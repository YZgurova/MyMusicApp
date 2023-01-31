package com.MyMusic.v1.api.rest.models;

import com.MyMusic.v1.services.models.Role;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class UserDto {
    public final Integer id;
    public final String firstName;
    public final String lastName;
    public final String username;
    public final String email;

    @JsonCreator
    public UserDto(
            @JsonProperty("id") Integer id,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("username") String username,
            @JsonProperty("email") String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
    }
}
