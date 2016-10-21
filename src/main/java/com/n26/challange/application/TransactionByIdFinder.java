package com.n26.challange.application;

import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.n26.challange.domain.Transaction;
import com.n26.challange.domain.TransactionRepository;

@Component
class TransactionByIdFinder {

    private final TransactionRepository transactionRepository;

    @Autowired
    TransactionByIdFinder(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction getTransactionOrThrowExceptionIfNotFound(long transactionId, Supplier<RuntimeException> exceptionSupplier) {
        Optional<Transaction> transaction = transactionRepository.getTransaction(transactionId);
        return transaction.orElseThrow(exceptionSupplier);
    }    
}
