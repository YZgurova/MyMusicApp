package com.MyMusic.v1.services.models;

import java.util.List;

public class Credentials {
    public final int id;
    public final String username;
    public final List<Role> roles;

    public Credentials(int id, String username, List<Role> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}
