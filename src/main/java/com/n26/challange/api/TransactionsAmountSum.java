package com.n26.challange.api;

import java.math.BigDecimal;

class TransactionsAmountSum {

    private final BigDecimal sum;

    public TransactionsAmountSum(BigDecimal sum) {
        this.sum = sum;
    }

    public BigDecimal getSum() {
        return sum;
    }
}
