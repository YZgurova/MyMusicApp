package com.MyMusic.v1.repositories.entities;

import com.MyMusic.v1.services.models.Role;

import java.util.List;

public class CredentialsEntity {
    public final int id;
    public final String username;
    public final String passwordHash;
    public final List<Role> roles;

    public CredentialsEntity(int id, String username, String passwordHash, List<Role> roles) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.roles = roles;
    }
}
