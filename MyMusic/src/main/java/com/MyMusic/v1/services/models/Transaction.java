package com.MyMusic.v1.services.models;

public class Transaction {
    public final int id;
    public final int userId;
    public final String transactionDescription;
    public final String Status;

    public Transaction(int id, int userId, String transactionDescription, String status) {
        this.id = id;
        this.userId = userId;
        this.transactionDescription = transactionDescription;
        Status = status;
    }
}
