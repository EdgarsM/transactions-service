package com.n26.challange.application;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

import com.n26.challange.domain.Transaction;
import com.n26.challange.domain.TransactionBuilder;

public class TransactionToDtoMapperTest {
    
    TransactionToDtoMapper transactionToDtoMapper = new TransactionToDtoMapper();

    @Test
    public void shouldTransactionToTransactionDto() {
        //given
        Transaction parentTransaction = TransactionBuilder.get()
                .withId(1L)
                .withAmount(BigDecimal.ONE)
                .withType("household")
                .build();
        Transaction transaction = TransactionBuilder.get()
                .withId(2L)
                .withAmount(BigDecimal.valueOf(12.34))
                .withType("cars")
                .withParentTransaction(parentTransaction)
                .build();
        
        //when
        TransactionDto transactionDto = transactionToDtoMapper.toDto(transaction);
        
        //then
        assertThat(transactionDto.getAmount(), comparesEqualTo(transaction.getAmount()));
        assertThat(transactionDto.getType(), is(transaction.getType()));
        assertThat(transactionDto.getParentId(), is(parentTransaction.getId()));
    }
}