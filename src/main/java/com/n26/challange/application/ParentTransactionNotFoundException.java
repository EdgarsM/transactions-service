package com.n26.challange.application;

public class ParentTransactionNotFoundException extends RuntimeException {

    public ParentTransactionNotFoundException(long parentTransactionId) {
        super(String.format("Parent transaction with id %d not found", parentTransactionId));
    }
}
