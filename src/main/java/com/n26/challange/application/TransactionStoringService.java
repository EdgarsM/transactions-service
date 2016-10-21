package com.n26.challange.application;

public interface TransactionStoringService {
    
    void storeTransaction(Long transactionId, TransactionDto transaction);
}
