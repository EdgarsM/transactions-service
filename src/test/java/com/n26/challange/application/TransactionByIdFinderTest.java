package com.n26.challange.application;

import static java.util.Optional.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;
import java.util.function.Supplier;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.n26.challange.domain.Transaction;
import com.n26.challange.domain.TransactionRepository;

@RunWith(MockitoJUnitRunner.class)
public class TransactionByIdFinderTest {

    @Mock
    TransactionRepository transactionRepository;
            
    @InjectMocks
    TransactionByIdFinder transactionByIdFinder;

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Test
    public void shouldReturnTransactionIfFoundBySpecifiedId() {
        //given
        Long transactionId = 123L;
        Transaction expectedTransaction = mock(Transaction.class);
        given(transactionRepository.getTransaction(transactionId)).willReturn(Optional.of(expectedTransaction));
        
        //when
        Transaction foundTransaction = transactionByIdFinder.getTransactionOrThrowExceptionIfNotFound(transactionId, 
                () -> new RuntimeException("Should not be called"));

        //then
        assertThat(foundTransaction, is(expectedTransaction));
    }
    
    @Test
    public void shouldThrowSuppliedExceptionIfTransactionNotFoundBySpecifiedId() {
        //given
        Long transactionId = 123L;
        given(transactionRepository.getTransaction(transactionId)).willReturn(empty());
        Supplier<RuntimeException> exceptionSupplier = () -> new TransactionNotFoundException(transactionId);
        // Because of the fact that JUnit ExpectedException rule requires to define expected exception before
        // class under test is called we can not use true BDD approach. That's one of the reasons I personally
        // prefer Groovy + Spock instead of JUnit + Mockito.
        // See as an example TransactionByIdFinderSpec.groovy
        thrown.expect(TransactionNotFoundException.class);
        thrown.expectMessage(transactionId.toString());
        
        //when
        transactionByIdFinder.getTransactionOrThrowExceptionIfNotFound(transactionId, exceptionSupplier);
    }
}