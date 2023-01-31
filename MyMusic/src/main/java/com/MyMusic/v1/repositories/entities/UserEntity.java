package com.MyMusic.v1.repositories.entities;

public class UserEntity {
    public final int id;
    public final String firstName;
    public final String lastName;
    public final String username;
    public final String email;
    public final String password_hash;
//    private final Boolean enabled =false;

    public UserEntity(int id, String firstName, String lastName, String username, String email, String password) {
        this.id=id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password_hash = password;
    }
}
