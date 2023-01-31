package com.MyMusic.v1.bin.dbcli;

import com.MyMusic.v1.repositories.MusicCreatorRepository;
import com.MyMusic.v1.repositories.SongRepository;
import com.MyMusic.v1.repositories.UserRepository;
import com.MyMusic.v1.repositories.mysql.MySQLMusicCreatorRepository;
import com.MyMusic.v1.repositories.mysql.MySQLSongRepository;
import com.MyMusic.v1.repositories.mysql.MySQLUserRepository;
import com.MyMusic.v1.services.MusicCreatorService;
import com.MyMusic.v1.services.SongService;
import com.MyMusic.v1.services.UserService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/mymusic_db","root","root");

        DataSource dataSource = new SingleConnectionDataSource(conn, false);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        TransactionTemplate txTemplate = new TransactionTemplate(transactionManager);

        UserRepository userRepository = new MySQLUserRepository(txTemplate, jdbcTemplate);
        MusicCreatorRepository musicCreatorRepository = new MySQLMusicCreatorRepository(txTemplate, jdbcTemplate);
        SongRepository songRepository = new MySQLSongRepository(txTemplate, jdbcTemplate);


        UserService userService = new UserService(userRepository);
//        MusicCreatorService creatorService = new MusicCreatorService(txTemplate, musicCreatorRepository, walletRepository);
        SongService songService = new SongService(songRepository);

        //userService.createUser("Hristo", "Zgurov", "HZ", "hz@abv.bg", BCrypt.hashpw("12345", BCrypt.gensalt()), 1);
        //userService.createUser("Rosi", "Zgurova", "RZ", "ee@dk.bg", "456789", 2);
        //System.out.println(userService.getUser(2).toString());
        //userService.deleteUser(3);
        //List<User> users = userService.listUsers(1, 3);
        //System.out.println(users);

        //creatorService.createCreator(1);
        //System.out.println(creatorService.getCreator(1).toString());
        //List<MusicCreator> creators = creatorService.listCreators(0,3);
        //System.out.println(creators);
        //creatorService.deleteCreator(1);

        //songService.createSong("Love me like you do", "url", 1, "Love");
        //System.out.println(songService.getSong(1));
        //List<Song> songs = songService.listSongs(0, 2);
        //System.out.println(songs);




    }
}
