package com.MyMusic.v1.repositories.mysql;

import com.MyMusic.v1.repositories.MusicCreatorRepository;
import com.MyMusic.v1.repositories.entities.MusicCreatorEntity;
import com.MyMusic.v1.repositories.entities.SongEntity;
import com.MyMusic.v1.repositories.entities.UserEntity;
import com.MyMusic.v1.services.models.Role;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MySQLMusicCreatorRepository implements MusicCreatorRepository {

    private final TransactionTemplate txTemplate;
    private final JdbcTemplate jdbc;

    public MySQLMusicCreatorRepository(TransactionTemplate txTemplate, JdbcTemplate jdbc) {
        this.txTemplate = txTemplate;
        this.jdbc = jdbc;
    }

    @Override
    public void createCreator(int userId) {
            jdbc.update(
                    "INSERT INTO credentials_to_roles (credentials_id, role_id) " +
                            "VALUES (?, ?)", userId, Role.CREATOR.id);
    }

    @Override
    public MusicCreatorEntity getCreator(int id) {
        return jdbc.queryForObject(Queries.GET_CREATOR, (rs, rowNum) -> fromResultSet(rs), id);
    }

    @Override
    public List<SongEntity> listCreatorSongs(int id, int lastShowedSong, int countSongs) {
        return jdbc.query(Queries.LIST_CREATOR_SONGS,
                (rs, rowNum) -> new SongEntity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("url"),
                        rs.getString("author"),
                        rs.getBigDecimal("price"),
                        rs.getInt("votes")
                ), id);
    }

    @Override
    public List<MusicCreatorEntity> listCreator(int lastShowedCreatorId, int countCreators) {
        return jdbc.query(Queries.LIST_CREATOR, (rs, rowNum)->fromResultSet(rs),Role.CREATOR.id, lastShowedCreatorId, countCreators);
    }

    @Override
    public void deleteCreator(int userId) {
        int id = jdbc.queryForObject("SELECT c.id from credentials as c \n" +
                "Join user as u\n" +
                "ON c.username=u.username\n" +
                "where u.id=?", (rs, rowNum) -> {
            return rs.getInt("id");
        }, userId);
        txTemplate.execute(status -> {
           jdbc.update(Queries.DELETE_CREATOR,id);
           return null;
        });
    }

    private MusicCreatorEntity fromResultSet(ResultSet rs) throws SQLException {
        return new MusicCreatorEntity(
            new UserEntity(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password")
            )
        );
    }

    static class Queries {
        public static final String GET_CREATOR = """
                select u.id, u.first_name, u.last_name, u.username, u.email, u.password
                FROM user as u        
                WHERE u.id=?;""";
        public static final String LIST_CREATOR = """
                select u.id, u.first_name, u.last_name, u.username, u.email, u.password
                  FROM user as u
                  JOIN credentials as c
                  ON u.username=c.username
                  JOIN credentials_to_roles as cr
                  ON c.id=cr.credentials_id
                  WHERE cr.role_id=? AND
                  (c.id>?)
                  LIMIT ?;""";

        public static final String LIST_CREATOR_SONGS = """
                SELECT s.id, s.name, s.url, u.username as author, s.price, s.votes
                FROM song as s
                JOIN user as u
                ON s.author_id = u.id
                WHERE u.id=?;
                """;
        public static final String DELETE_CREATOR =
                "DELETE FROM credentials_to_roles WHERE credentials_id=?;";
    }
}
