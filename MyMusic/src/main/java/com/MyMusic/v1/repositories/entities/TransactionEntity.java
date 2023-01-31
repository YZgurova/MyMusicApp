package com.MyMusic.v1.repositories.entities;

public class TransactionEntity {
    public final int id;
    public final int userId;
    public final String transactionDescription;
    public final String Status;

    public TransactionEntity(int id, int userId, String transactionDescription, String status) {
        this.id = id;
        this.userId = userId;
        this.transactionDescription = transactionDescription;
        Status = status;
    }
}
