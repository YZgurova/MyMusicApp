package com.MyMusic.v1.repositories.mysql;

import com.MyMusic.v1.repositories.SongRepository;
import com.MyMusic.v1.repositories.entities.MusicCreatorEntity;
import com.MyMusic.v1.repositories.entities.SongEntity;
import com.MyMusic.v1.services.models.Role;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MySQLSongRepository implements SongRepository {

    private final TransactionTemplate txTemplate;
    private final JdbcTemplate jdbc;

    public MySQLSongRepository(TransactionTemplate txTemplate, JdbcTemplate jdbcT) {
        this.txTemplate = txTemplate;
        this.jdbc = jdbcT;
    }

    @Override
    public SongEntity createSong(String name, String url, int authorId, BigDecimal price) {
        return txTemplate.execute(status -> {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbc.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(
                    Queries.INSERT_SONG, Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1,name);
                ps.setString(2,url);
                ps.setInt(3,authorId);
                ps.setBigDecimal(4,price);
                return ps;
            }, keyHolder);
            int id = Objects.requireNonNull(keyHolder.getKey().intValue());
            return getSong(id);
        });
    }

    @Override
    public SongEntity addToBoughtSongs(int userId, int songId) {
        return txTemplate.execute(status -> {
            jdbc.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(
                        Queries.ADD_BOUGHT_SONG, Statement.RETURN_GENERATED_KEYS
                );
                ps.setInt(1,userId);
                ps.setInt(2,songId);
                return ps;
            });
            return getSong(songId);
        });

    }

    @Override
    public SongEntity addToLovedSongs(int userId, int songId) {
        return txTemplate.execute(status -> {
            jdbc.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(
                        Queries.ADD_lOVED_SONG, Statement.RETURN_GENERATED_KEYS
                );
                ps.setInt(1,userId);
                ps.setInt(2,songId);
                return ps;
            });
            return getSong(songId);
        });
    }

    @Override
    public void removedLovedSongs(int userId, int songId) {
        txTemplate.execute(status -> {
            jdbc.update(Queries.DELETE_LOVED_SONG, userId, songId);
            return null;
        });
    }

    @Override
    public void updateSongVotes(int songId, int votes) {
        jdbc.update("UPDATE `song` SET `votes` = ? WHERE (`id` = ?);", votes, songId);
    }

    @Override
    public SongEntity getSong(int id) {
        return jdbc.queryForObject(Queries.GET_SONG, (rs, rowNum) -> fromResultSet(rs), id);
    }

    @Override
    public int getAutorId(int songId) {
        return jdbc.queryForObject(Queries.GET_AUTHOR_ID, (rs, rowNum) -> rs.getInt("author_id"), songId);
    }

    @Override
    public List<SongEntity> listAllSong(int lastShowedSongId, int countSongs) {
        return jdbc.query(Queries.LIST_ALL_SONGS, (rs, rowNum) -> fromResultSet(rs), lastShowedSongId, countSongs);
    }

    @Override
    public List<SongEntity> listBoughtSongs(int userId) {
        return jdbc.query(Queries.LIST_BOUGHT_SONGS, (rs, rowNum) -> fromResultSet(rs), userId);
    }

    @Override
    public List<SongEntity> listLovedSongs(int userId) {
        return jdbc.query(Queries.LIST_LOVED_SONGS, (rs, rowNum) -> fromResultSet(rs), userId);
    }

    @Override
    public List<SongEntity> listRecommendedSongs(int userId, int lastShowedSong, int countSongs) {
        List<SongEntity> allSongs = jdbc.query(Queries.LIST_ALL_SONGS, (rs, rowNum) -> fromResultSet(rs), lastShowedSong, countSongs);
        List<SongEntity> lovedSongs = jdbc.query(Queries.LIST_LOVED_SONGS, (rs, rowNum) -> fromResultSet(rs), userId);
        List<SongEntity> boughtSongs = jdbc.query(Queries.LIST_BOUGHT_SONGS, (rs, rowNum) -> fromResultSet(rs), userId);
        List<Object> creatorId = jdbc.query("SELECT credentials_id FROM credentials_to_roles \n" +
                                                              "WHERE credentials_id=? AND role_id=?;", (rs, rowNum) -> rs.getInt("credentials_id"), userId, Role.CREATOR.id);
        if(creatorId.size()>0) {
            List<SongEntity> mySongs= jdbc.query(Queries.LIST_CREATOR_SONGS, (rs, rowNum) -> fromResultSet(rs), userId);
            for (SongEntity song : allSongs) {
                if (mySongs.contains(song)) {
                    song.setLoved(true);
                    song.setBought(true);
                }
            }
        }

        for (SongEntity song : allSongs) {
            if (lovedSongs.contains(song)) {
                song.setLoved(true);
            }
            if (boughtSongs.contains(song)) {
                song.setBought(true);
            }
        }
        return  allSongs;
    }

    @Override
    public void deleteSong(int id) {
        txTemplate.execute(status -> {
            jdbc.update(Queries.DELETE_SONG, id);
            return null;
        });
    }

    private SongEntity fromResultSet(ResultSet rs) throws SQLException {
        return new SongEntity(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("url"),
                rs.getString("author"),
                rs.getBigDecimal("price"),
                rs.getInt("votes")
        );
    }

    private static class Queries {
        public static final String INSERT_SONG =
                "INSERT INTO song (name, url, author_id, price) VALUES (?,?,?,?)";

        public static final String ADD_BOUGHT_SONG =
                "INSERT INTO bought_song_to_user (user_id, song_id) VALUES (?,?)";

        public static final String ADD_lOVED_SONG =
                "INSERT INTO loved_song_to_user (user_id, song_id) VALUES (?,?)";

        public static final String DELETE_LOVED_SONG =
                "DELETE FROM loved_song_to_user WHERE user_id=? AND song_id=?";

        public static final String GET_AUTHOR_ID =
                "SELECT author_id from song WHERE id=?";

        public static final String GET_SONG = """
                        SELECT s.id, s.name, s.url, u.username as author, s.price, s.votes
                        FROM song as s
                        JOIN user as u
                        ON s.author_id=u.id
                        WHERE s.id=?;""";
        public static final String LIST_ALL_SONGS = """
                        SELECT s.id, s.name, s.url, u.username as author, s.price, s.votes
                        FROM song as s
                        JOIN user as u
                        ON s.author_id=u.id
                        WHERE (s.id>?)
                        LIMIT ?;""";

        public static final String LIST_LOVED_SONGS = """
                SELECT s.id, s.name, s.url, u.username as author, s.price, s.votes
                FROM song as s
                JOIN user as u
                ON s.author_id=u.id
                JOIN loved_song_to_user as l
                ON l.song_id=s.id
                WHERE l.user_id=?;""";

        public static final String LIST_BOUGHT_SONGS = """
                SELECT s.id, s.name, s.url, u.username as author, s.price, s.votes
                FROM song as s
                JOIN user as u
                ON s.author_id=u.id
                JOIN bought_song_to_user as b
                ON b.song_id=s.id
                WHERE b.user_id=?""";

        public static final String LIST_CREATOR_SONGS= """
                SELECT s.id, s.name, s.url, u.username as author, s.price, s.votes
                FROM song as s
                JOIN user as u
                ON s.author_id = u.id
                WHERE u.id=?;""";

        public static final String DELETE_SONG =
                "DELETE FROM song WHERE id=?";
    }
}
