package com.MyMusic.v1.repositories.mysql;

import com.MyMusic.v1.repositories.WalletRepository;
import com.MyMusic.v1.repositories.entities.WalletEntity;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLWalletRepository implements WalletRepository {
    private final JdbcTemplate jdbc;

    public MySQLWalletRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public WalletEntity createWallet(int ownerId) {
            jdbc.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO wallet (owner_id) VALUES (?)"
                        , Statement.RETURN_GENERATED_KEYS
                );
                ps.setInt(1,ownerId);
                return ps;
            });
            return getWallet(ownerId);
    }

    @Override
    public void addMoney(int ownerId, BigDecimal amount) {
        jdbc.update("UPDATE `wallet` SET `resources` = ? WHERE (`owner_id` = ?);", amount, ownerId);
    }

    @Override
    public WalletEntity getWallet(int ownerId) {
        return jdbc.queryForObject(
                "SELECT owner_id, resources FROM wallet WHERE owner_id=?;",
                (rs, rowNum) -> fromResultSet(rs), ownerId);
    }

    private WalletEntity fromResultSet(ResultSet rs) throws SQLException {
        return new WalletEntity(
                rs.getInt("owner_id"),
                rs.getBigDecimal("resources")
        );
    }
}
