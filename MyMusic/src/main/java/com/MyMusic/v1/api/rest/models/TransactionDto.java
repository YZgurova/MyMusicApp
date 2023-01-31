package com.MyMusic.v1.api.rest.models;

public class TransactionDto {
    public final int id;
    public final int userId;
    public final String transactionDescription;
    public final String Status;

    public TransactionDto(int id, int userId, String transactionDescription, String status) {
        this.id = id;
        this.userId = userId;
        this.transactionDescription = transactionDescription;
        Status = status;
    }
}
