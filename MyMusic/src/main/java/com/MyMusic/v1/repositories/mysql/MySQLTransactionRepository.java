package com.MyMusic.v1.repositories.mysql;

import com.MyMusic.v1.repositories.TransactionsRepository;
import com.MyMusic.v1.repositories.entities.TransactionEntity;
import com.MyMusic.v1.repositories.entities.UserEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class MySQLTransactionRepository implements TransactionsRepository {

    private final TransactionTemplate txTemplate;
    private final JdbcTemplate jdbc;

    public MySQLTransactionRepository(TransactionTemplate txTemplate, JdbcTemplate jdbc) {
        this.txTemplate = txTemplate;
        this.jdbc = jdbc;
    }

    @Override
    public TransactionEntity createTransaction(int userId, String transaction, String transactionStatus) {
        return txTemplate.execute(status -> {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbc.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO transactions (user_id, transaction, status) VALUES (?,?,?)"
                , Statement.RETURN_GENERATED_KEYS
                );
                ps.setInt(1,userId);
                ps.setString(2,transaction);
                ps.setString(3, transactionStatus);
                return ps;
            }, keyHolder);

            int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
            return getTransaction(id);
        });
    }

    @Override
    public TransactionEntity getTransaction(int transactionId) {
        return jdbc.queryForObject(
                "SELECT id, user_id, transaction, status FROM transactions WHERE id=?;",
                (rs, rowNum) -> fromResultSet(rs), transactionId);
    }

    private TransactionEntity fromResultSet(ResultSet rs) throws SQLException {
        return new TransactionEntity(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("transaction"),
                rs.getString("status")
        );
    }
}
