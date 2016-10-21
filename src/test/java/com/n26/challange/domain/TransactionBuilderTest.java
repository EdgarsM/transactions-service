package com.n26.challange.domain;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TransactionBuilderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldBuildTransaction() {
        //given
        TransactionBuilder transactionBuilder = TransactionBuilder.get()
                .withId(123L)
                .withAmount(BigDecimal.ONE)
                .withType("cars");

        //when
        Transaction transaction = transactionBuilder.build();
        
        assertThat(transaction.getId(), is(123L));
        assertThat(transaction.getAmount(), is(BigDecimal.ONE));
        assertThat(transaction.getType(), is("cars"));
    }

    @Test
    public void shouldBuildTransactionWithReferenceToParent() {
        //given
        Transaction parentTransaction = transaction(1L);
        TransactionBuilder transactionBuilder = TransactionBuilder.get()
                .withId(123L)
                .withAmount(BigDecimal.ONE)
                .withType("cars")
                .withParentTransaction(parentTransaction);

        //when
        Transaction transaction = transactionBuilder.build();

        assertThat(transaction.getId(), is(123L));
        assertThat(transaction.getAmount(), is(BigDecimal.ONE));
        assertThat(transaction.getType(), is("cars"));
        assertThat(transaction.getParentId(), is(parentTransaction.getId()));
    }

    private Transaction transaction(Long id) {
        Transaction transaction = mock(Transaction.class);
        given(transaction.getId()).willReturn(id);
        return transaction;
    }

    @Test
    public void shouldThrowExceptionIfIdNotSpecified() {
        //given & then
        TransactionBuilder transactionBuilder = TransactionBuilder.get()
                .withAmount(BigDecimal.ONE)
                .withType("cars");
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("id");

        //when
        transactionBuilder.build();
    }
    
    @Test
    public void shouldThrowExceptionIfAmountNotSpecified() {
        //given & then
        TransactionBuilder transactionBuilder = TransactionBuilder.get()
                .withId(123L)
                .withType("cars");
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("amount");
        
        //when
        transactionBuilder.build();
    }

    @Test
    public void shouldThrowExceptionIfTypeNotSpecified() {
        //given & then
        TransactionBuilder transactionBuilder = TransactionBuilder.get()
                .withId(123L)
                .withAmount(BigDecimal.ONE);
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("type");

        //when
        transactionBuilder.build();
    }
}