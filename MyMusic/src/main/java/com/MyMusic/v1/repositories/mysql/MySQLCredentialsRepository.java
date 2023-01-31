package com.MyMusic.v1.repositories.mysql;

import com.MyMusic.v1.repositories.CredentialsRepository;
import com.MyMusic.v1.repositories.entities.CredentialsEntity;
import com.MyMusic.v1.services.models.Role;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class MySQLCredentialsRepository implements CredentialsRepository {
    private final TransactionTemplate txTemplate;
    private final JdbcTemplate jdbc;

    public MySQLCredentialsRepository(TransactionTemplate txTemplate, JdbcTemplate jdbc) {
        this.txTemplate = txTemplate;
        this.jdbc = jdbc;
    }

    @Override
    public CredentialsEntity createCredentials(String username, String passwordHash, List<Role> roles) {
        return txTemplate.execute(status -> {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbc.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO credentials (username, password_hash) VALUES (?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, username);
                ps.setString(2, passwordHash);
                return ps;
            }, keyHolder);

            int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
            for (Role r : roles) {
                jdbc.update(
                        "INSERT INTO credentials_to_roles (credentials_id, role_id) " +
                                "VALUES (?, ?)", id, r.id);
            }

            return new CredentialsEntity(id, username, passwordHash, roles);
        });
    }

    @Override
    public String createAuthToken(Integer credentialsId, String authToken) {
        jdbc.update("INSERT INTO auth_tokens (token, credentials_id) " +
                "VALUES (?, ?)", authToken, credentialsId);
        return authToken;
    }

    @Override
    public Optional<CredentialsEntity> getCredentials(String username) {
        return txTemplate.execute(status -> {
            Map<String, Object> creds = jdbc.queryForMap(
                    "SELECT id, username, password_hash " +
                            "FROM credentials WHERE username = ?", username);
            // TODO: Throw exception when creds not found, but dunno what exception
            // is returned. Need to test this.

            List<Integer> roleIDs = jdbc.query(
                    "SELECT role_id FROM credentials_to_roles WHERE credentials_id = ?",
                    (rs, rowNum) -> {
                        return rs.getInt("role_id");
                    }, creds.get("id"));

            List<Role> roles = roleIDs.stream().
                    map((id) -> switch (id) {
                        case 1 -> Role.USER;
                        case 2 -> Role.ADMIN;
                        case 3 -> Role.CREATOR;
                        default -> throw new RuntimeException("invalid role id");
                    }).
                    collect(Collectors.toList());
            return Optional.of(
                    new CredentialsEntity(
                            (int)creds.get("id"),
                            (String) creds.get("username"),
                            (String) creds.get("password_hash"),
                            roles));
        });
    }

    @Override
    public Optional<CredentialsEntity> getCredentialsByAuthToken(String authToken) {
//        Objects.requireNonNull(authToken);
        return txTemplate.execute(status -> {
            Map<String, Object> creds = jdbc.queryForMap(
                    "SELECT c.id as id, c.username as username, c.password_hash as password_hash " +
                            "FROM credentials c " +
                            "JOIN auth_tokens at ON c.id = at.credentials_id " +
                            "WHERE at.token = ?", authToken);
            // TODO: Throw exception when creds not found, but dunno what exception
            // is returned. Need to test this.

            List<Integer> roleIDs = jdbc.query(
                    "SELECT role_id FROM credentials_to_roles WHERE credentials_id = ?",
                    (rs, rowNum) -> {
                        return rs.getInt("role_id");
                    }, creds.get("id"));

            List<Role> roles = roleIDs.stream().
                    map((id) -> switch (id) {
                        case 1 -> Role.USER;
                        case 2 -> Role.ADMIN;
                        case 3 -> Role.CREATOR;
                        default -> throw new RuntimeException("invalid role id");
                    }).
                    collect(Collectors.toList());
            return Optional.of(
                    new CredentialsEntity(
                            (int)creds.get("id"),
                            (String) creds.get("username"),
                            (String) creds.get("password_hash"),
                            roles));
        });
    }
}
