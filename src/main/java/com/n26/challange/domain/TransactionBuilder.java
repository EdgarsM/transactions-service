package com.n26.challange.domain;

import static java.util.Objects.*;

import java.math.BigDecimal;

public class TransactionBuilder {
    
    private Long id;
    
    private BigDecimal amount;
    
    private String type;
    
    private Transaction parentTransaction;
    
    public static TransactionBuilder get() {
        return new TransactionBuilder();
    }

    public TransactionBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public TransactionBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public TransactionBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public TransactionBuilder withParentTransaction(Transaction parentTransaction) {
        this.parentTransaction = parentTransaction;
        return this;
    }
    
    public Transaction build() {
        requireNonNull(id, "Transaction id is mandatory");
        requireNonNull(amount, "Transaction amount is mandatory");
        requireNonNull(type, "Transaction type is mandatory");
        if (parentTransaction == null) {
            return new Transaction(id, amount, type);
        } else {
            return new Transaction(id, amount, type, parentTransaction);
        }
    }
}
