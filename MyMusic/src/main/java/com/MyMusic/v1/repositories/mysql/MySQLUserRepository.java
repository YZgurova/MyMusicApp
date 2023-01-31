package com.MyMusic.v1.repositories.mysql;

import com.MyMusic.v1.repositories.UserRepository;
import com.MyMusic.v1.repositories.entities.UserEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MySQLUserRepository implements UserRepository {

    private final TransactionTemplate txTemplate;
    private final JdbcTemplate jdbc;

    public MySQLUserRepository(TransactionTemplate txTemplate, JdbcTemplate jdbc) {
        this.txTemplate = txTemplate;
        this.jdbc = jdbc;
    }

    @Override
    public UserEntity createUser(Integer id, String firstName, String lastName, String username, String email, String password) {
        return txTemplate.execute(status -> {
            jdbc.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(
                        Queries.INSERT_USER, Statement.RETURN_GENERATED_KEYS
                );
                ps.setInt(1,id);
                ps.setString(2,firstName);
                ps.setString(3, lastName);
                ps.setString(4,username);
                ps.setString(5, email);
                ps.setString(6, password);
                return ps;
            });
            return getUser(id);
        });
    }

    @Override
    public int updateUser(int id, String firstName, String lastName, String username, String email) {
        return jdbc.update(Queries.UPDATE_USER, id, firstName, lastName, username, email);
    }

    @Override
    public UserEntity getUserByUsername(String username) {
        return jdbc.queryForObject(Queries.GET_USER_BY_USERNAME, (rs, rowNum) -> fromResultSet(rs), username);
    }

    @Override
    public UserEntity getUser(int id) {
        return jdbc.queryForObject(Queries.GET_USER, (rs, rowNum) -> fromResultSet(rs), id);
    }

    @Override
    public List<UserEntity> listUsers(int lastShowedUserId, int countUsers) {
        return jdbc.query(Queries.LIST_USERS, (rs, rowNum) -> fromResultSet(rs), lastShowedUserId, countUsers);
    }

    @Override
    public void deleteUser(int id) {
        txTemplate.execute(status -> {
           jdbc.update(Queries.DELETE_USER, id);
           return null;
        });
    }
    private UserEntity fromResultSet(ResultSet rs) throws SQLException {
        return new UserEntity(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password")
        );
    }

    static class Queries {
        public static final String INSERT_USER =
                "INSERT INTO user (id, first_name, last_name, username, email, password)" +
                        "VALUES (?,?,?,?,?,?)";
        public static final String GET_USER =
                """
                select u.id, u.first_name, u.last_name,u.username, u.email,u.password
                FROM user as u
                WHERE u.id=?;""";

        public static final String GET_USER_BY_USERNAME =
                """
                select u.id, u.first_name, u.last_name,u.username, u.email,u.password
                FROM user as u
                WHERE u.username=?;""";

        public static final String LIST_USERS =
                """
                select u.id, u.first_name, u.last_name,u.username, u.email,u.password
                FROM user as u
                WHERE (u.id>?)
                LIMIT ?;""";

        public static final String UPDATE_USER =
                """
                UPDATE user as u SET u.first_name=?, u.last_name=?,u.username=?, u.email=? WHERE (`id` = ?);
                """;

        public  static final String DELETE_USER = "DELETE FROM user WHERE id=?";

    }
}
