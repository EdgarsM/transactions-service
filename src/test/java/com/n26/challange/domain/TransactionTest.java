package com.n26.challange.domain;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class TransactionTest {
    
    long lastTransactionId;

    @Test
    public void shouldAddChildTransaction() {
        //given
        Transaction parentTransaction = new Transaction(1L, BigDecimal.TEN, "all");
        Transaction transaction = new Transaction(2L, BigDecimal.ONE, "cars", parentTransaction);
        assertThat(parentTransaction.getAmountIncludedLinkedTransactionAmounts(), comparesEqualTo(parentTransaction.getAmount()));
        
        //when
        transaction.joinParentChildCollection();    
        
        //then
        assertThat(parentTransaction.getAmountIncludedLinkedTransactionAmounts(), comparesEqualTo(BigDecimal.valueOf(11)));
    }

    @Test
    public void shouldCalculateTotalAmountIncludedLinkedTransactionAmounts() {
        //given
        Transaction grandParentTransaction = new Transaction(1L, BigDecimal.TEN, "all");
        Transaction firstParentTransaction = transaction(BigDecimal.ONE, grandParentTransaction);
        transaction(BigDecimal.valueOf(0.70), firstParentTransaction);
        transaction(BigDecimal.valueOf(0.30), firstParentTransaction);
        Transaction secondParentTransaction = transaction(BigDecimal.valueOf(2), grandParentTransaction);
        transaction(BigDecimal.valueOf(1), secondParentTransaction);
        transaction(BigDecimal.valueOf(3), grandParentTransaction);
        
        //when
        BigDecimal totalAmount = grandParentTransaction.getAmountIncludedLinkedTransactionAmounts();

        //then
        assertThat(totalAmount, comparesEqualTo(BigDecimal.valueOf(18)));
    }
    
    @Test
    public void shouldBeEqualTransactionsWithEqualId() {
        //given
        Transaction firstTransaction = new Transaction(1L, BigDecimal.TEN, "some");
        Transaction secondTransaction = new Transaction(1L, BigDecimal.ONE, "other");
        
        //when
        boolean equals = firstTransaction.equals(secondTransaction);
        
        //then
        assertThat(equals, is(true));
    }
    
    private Transaction transaction(BigDecimal amount, Transaction parentTransaction) {
        Transaction transaction = new Transaction(lastTransactionId++, amount, "cars", parentTransaction);
        transaction.joinParentChildCollection();
        return transaction;
    }
}