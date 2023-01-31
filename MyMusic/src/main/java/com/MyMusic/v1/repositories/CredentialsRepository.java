package com.MyMusic.v1.repositories;

import com.MyMusic.v1.repositories.entities.CredentialsEntity;
import com.MyMusic.v1.services.models.Role;

import java.util.List;
import java.util.Optional;
public interface CredentialsRepository {
    CredentialsEntity createCredentials(String username, String passwordHash, List<Role> roles);

    String createAuthToken(Integer credentialsId, String authToken);
    Optional<CredentialsEntity> getCredentials(String username);
    Optional<CredentialsEntity> getCredentialsByAuthToken(String authToken);
}
