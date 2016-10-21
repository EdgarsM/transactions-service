package com.n26.challange.application;

import java.math.BigDecimal;
import java.util.Collection;

public interface TransactionQueryingService {

    TransactionDto getTransaction(Long transactionId);
        
    Collection<Long> getTransactionsIdsByType(String transactionType);

    BigDecimal getSpecifiedAndLinkedTransactionsAmountSum(Long transactionId);
}