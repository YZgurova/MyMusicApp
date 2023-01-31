package com.MyMusic.v1.beans;

import com.MyMusic.v1.repositories.*;
import com.MyMusic.v1.repositories.mysql.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class RepositoryBeans {
    @Bean
    public UserRepository userRepository(
            TransactionTemplate txTemplate, JdbcTemplate jdbcTemplate) {
        return new MySQLUserRepository(txTemplate, jdbcTemplate);
    }

    @Bean
    public MusicCreatorRepository creatorRepository(
            TransactionTemplate txTemplate, JdbcTemplate jdbcTemplate) {
        return new MySQLMusicCreatorRepository(txTemplate, jdbcTemplate);
    }

    @Bean
    public SongRepository songRepository(
            TransactionTemplate txTemplate, JdbcTemplate jdbcTemplate) {
        return new MySQLSongRepository(txTemplate, jdbcTemplate);
    }

    @Bean
    public WalletRepository walletRepository(JdbcTemplate jdbcTemplate) {
        return new MySQLWalletRepository(jdbcTemplate);
    }

    @Bean
    public TransactionsRepository transactionsRepository(TransactionTemplate txTemplate, JdbcTemplate jdbcTemplate) {
        return new MySQLTransactionRepository(txTemplate, jdbcTemplate);
    }

    @Bean
    public CredentialsRepository credentialsRepository(
            TransactionTemplate txTemplate, JdbcTemplate jdbcTemplate) {
        return new MySQLCredentialsRepository(txTemplate, jdbcTemplate);
    }
}
