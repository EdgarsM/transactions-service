package com.n26.challange.domain;

public class TransactionAlreadyStoredException extends RuntimeException {

    public TransactionAlreadyStoredException(long transactionId) {
        super(String.format("Transaction with id %d is already stored. Transaction modification is not allowed", transactionId));
    }
}
