package com.MyMusic.v1.services.models;

public enum Role {
    USER(1), ADMIN(2), CREATOR(3);

    public final int id;

    Role(int id) {
        this.id = id;
    }

    static Role fromID(int id) {
        return switch (id) {
            case 1 -> USER;
            case 2 -> ADMIN;
            case 3 -> CREATOR;
            default -> throw new RuntimeException("invalid role id");
        };
    }
}

