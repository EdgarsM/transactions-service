package com.n26.challange.application;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.function.Supplier;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;

import com.n26.challange.domain.Transaction;
import com.n26.challange.domain.TransactionRepository;

@RunWith(MockitoJUnitRunner.class)
public class TransactionStoringServiceImplTest {

    @Mock
    TransactionRepository transactionRepository;
    
    @Mock
    TransactionByIdFinder finder;
    
    @InjectMocks
    TransactionStoringServiceImpl transactionStoringService;
    
    @Captor
    ArgumentCaptor<Transaction> transactionCaptor;

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Test
    public void shouldStoreTransactionWithoutReferenceToParent() {
        //given
        Long transactionId = 321L;
        TransactionDto transactionDto = new TransactionDto(BigDecimal.TEN, "cars", null);
        
        //when
        transactionStoringService.storeTransaction(transactionId, transactionDto);
                
        //then
        verify(transactionRepository).store(transactionCaptor.capture());
        Transaction savedTransaction = transactionCaptor.getValue();
        assertThat(savedTransaction.getId(), is(transactionId));
        assertThat(savedTransaction.getAmount(), comparesEqualTo(transactionDto.getAmount()));
        assertThat(savedTransaction.getType(), is(transactionDto.getType()));
        assertThat(savedTransaction.getParentId(), nullValue());
        verifyZeroInteractions(finder);
    }

    @Test
    public void shouldStoreTransactionWithReferenceToParent() {
        //given
        Long transactionId = 321L;
        Long parentTransactionId = 123L;
        TransactionDto transactionDto = new TransactionDto(BigDecimal.TEN, "cars", parentTransactionId);
        Transaction parentTransaction = transaction(parentTransactionId);
        given(finder.getTransactionOrThrowExceptionIfNotFound(eq(parentTransactionId), any())).willReturn(parentTransaction);

        //when
        transactionStoringService.storeTransaction(transactionId, transactionDto);

        //then
        verify(transactionRepository).store(transactionCaptor.capture());
        Transaction savedTransaction = transactionCaptor.getValue();
        assertThat(savedTransaction.getId(), is(transactionId));
        assertThat(savedTransaction.getAmount(), comparesEqualTo(transactionDto.getAmount()));
        assertThat(savedTransaction.getType(), is(transactionDto.getType()));
        assertThat(savedTransaction.getParentId(), is(parentTransactionId));
    }

    @Test
    public void shouldThrowExceptionIfParentTransactionNotFoundBySpecifiedId() {
        //given & then
        Long transactionId = 321L;
        Long parentTransactionId = 123L;
        TransactionDto transactionDto = new TransactionDto(BigDecimal.TEN, "cars", parentTransactionId);
        given(finder.getTransactionOrThrowExceptionIfNotFound(eq(parentTransactionId), any())).willAnswer(
                (InvocationOnMock invocation) -> {throw ((Supplier<RuntimeException>) invocation.getArguments()[1]).get();}
        );
        thrown.expect(ParentTransactionNotFoundException.class);
        thrown.expectMessage(parentTransactionId.toString());

        //when
        transactionStoringService.storeTransaction(transactionId, transactionDto);
    }

    private Transaction transaction(long id) {
        Transaction transaction = mock(Transaction.class);
        given(transaction.getId()).willReturn(id);
        return transaction;
    }
}