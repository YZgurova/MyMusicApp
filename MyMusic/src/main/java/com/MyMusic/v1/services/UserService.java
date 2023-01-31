package com.MyMusic.v1.services;

import com.MyMusic.v1.repositories.UserRepository;
import com.MyMusic.v1.repositories.entities.UserEntity;
import com.MyMusic.v1.services.models.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Resource
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

//    public User createUser(String firstName, String lastName, String username, String email,String password) {
//        return Mappers.fromUserEntity(
//                repository.createUser(
//                        firstName,lastName,username,email,password));
//    }

    public int updateUser(int id, String firstName, String lastName, String username, String email) {
        return repository.updateUser(id,firstName,lastName,username,email);
    }

    public User getUser(int id) {
        UserEntity u = repository.getUser(id);
        return Mappers.fromUserEntity(u);
    }

    //public List<Song> listOfSongMix(int idOfMix) {}

    public List<User> listUsers(int lastShowedUserId, int coutUsers) {
        return repository.listUsers(lastShowedUserId,coutUsers)
                .stream()
                .map(Mappers::fromUserEntity)
                .collect(Collectors.toList());
    }

    public void deleteUser(int id) {
        repository.deleteUser(id);
    }


}
