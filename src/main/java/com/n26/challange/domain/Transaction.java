package com.n26.challange.domain;

import static java.util.Objects.*;
import static java.util.Optional.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Transaction {
    
    private final Long id;

    private final BigDecimal amount;
    
    private final String type;
    
    private final Optional<Transaction> parentTransaction;
    
    private final Set<Transaction> childTransactions = ConcurrentHashMap.newKeySet();

    Transaction(Long id, BigDecimal amount, String type) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.parentTransaction = empty();
    }

    Transaction(Long id, BigDecimal amount, String type, Transaction parentTransaction) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.parentTransaction = Optional.of(requireNonNull(parentTransaction, "Parent transaction is mandatory"));
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    private void addChildTransaction(Transaction childTransaction) {
        childTransactions.add(childTransaction);    
    }

    public Long getParentId() {
        return parentTransaction.map(Transaction::getId).orElse(null);
    }

    public BigDecimal getAmountIncludedLinkedTransactionAmounts() {
        return childTransactions.stream()
                .map(Transaction::getAmountIncludedLinkedTransactionAmounts)
                .reduce(amount, BigDecimal::add);
    }
    
    void joinParentChildCollection() {
        parentTransaction.ifPresent(parent -> parent.addChildTransaction(this));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
