package com.MyMusic.v1.api.rest.models;

import com.MyMusic.v1.services.models.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AuthenticationDTO {

    public final UserDto user;
    public final List<Role> roles;
    public final String token;

    public AuthenticationDTO(@JsonProperty ("user") UserDto user, @JsonProperty("roles") List<Role> roles, @JsonProperty ("token") String token) {
        this.user = user;
        this.token = token;
        this.roles = roles;
    }
}
