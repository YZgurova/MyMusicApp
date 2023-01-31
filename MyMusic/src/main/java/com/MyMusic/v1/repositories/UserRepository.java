package com.MyMusic.v1.repositories;

import com.MyMusic.v1.repositories.entities.UserEntity;

import java.util.List;

public interface UserRepository {
    UserEntity createUser(
            Integer id, String firstName, String lastName, String username, String email,
            String password);

    int updateUser(int id,
                   String firstName, String lastName, String username, String email);

    UserEntity getUserByUsername(String username);

    UserEntity getUser(int id);
    List<UserEntity> listUsers(int lastShowedUserId, int countUsers);
    void deleteUser(int id);

}
