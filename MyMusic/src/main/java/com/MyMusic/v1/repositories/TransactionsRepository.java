package com.MyMusic.v1.repositories;

import com.MyMusic.v1.repositories.entities.TransactionEntity;

public interface TransactionsRepository {
    TransactionEntity createTransaction(int userId, String transaction, String status);
    TransactionEntity getTransaction(int transactionId);
}
