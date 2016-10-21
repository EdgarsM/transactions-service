package com.n26.challange.application;

import org.springframework.stereotype.Component;

import com.n26.challange.domain.Transaction;

@Component
class TransactionToDtoMapper {

    TransactionDto toDto(Transaction transaction) {
        return new TransactionDto(transaction.getAmount(), transaction.getType(), transaction.getParentId());   
    }
}
