package com.n26.challange.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

public interface TransactionRepository {

    Optional<Transaction> getTransaction(long transactionId);

    void store(Transaction transaction);

    Collection<Long> getTransactionsIdsByType(String transactionType);

    BigDecimal getSpecifiedAndLinkedTransactionsAmountSum(Transaction transaction);
}
