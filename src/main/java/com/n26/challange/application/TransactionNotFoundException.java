package com.n26.challange.application;

public class TransactionNotFoundException extends RuntimeException {

    public TransactionNotFoundException(long transactionId) {
        super(String.format("Transaction not found by id %d", transactionId));
    }   
}
