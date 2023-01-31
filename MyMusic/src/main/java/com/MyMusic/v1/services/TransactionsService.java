package com.MyMusic.v1.services;

import com.MyMusic.v1.repositories.TransactionsRepository;
import com.MyMusic.v1.services.models.Transaction;

public class TransactionsService {
    TransactionsRepository repository;

    public TransactionsService(TransactionsRepository transactionsRepository) {
        this.repository = transactionsRepository;
    }

    public Transaction createTransaction(int userId, String transaction, String status) {
        return Mappers.fromTransactionEntity(repository.createTransaction(userId, transaction, status));
    }

    public Transaction getTransaction(int transactionId) {
        return Mappers.fromTransactionEntity(repository.getTransaction(transactionId));
    }
}
