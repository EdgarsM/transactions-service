package com.n26.challange.application;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;

import com.n26.challange.domain.Transaction;
import com.n26.challange.domain.TransactionRepository;

@RunWith(MockitoJUnitRunner.class)
public class TransactionQueryingServiceImplTest {

    @Mock
    TransactionRepository transactionRepository;
    
    @Mock
    TransactionByIdFinder transactionByIdFinder;
    
    @Mock
    TransactionToDtoMapper transactionToDtoMapper;

    @InjectMocks
    TransactionQueryingServiceImpl queryingService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldMapFoundTransactionToDto() {
        //given
        Long transactionId = 123L;
        Transaction foundTransaction = mock(Transaction.class);
        TransactionDto expectedTransactionDto = mock(TransactionDto.class);
        given(transactionByIdFinder.getTransactionOrThrowExceptionIfNotFound(eq(transactionId), any())).willReturn(foundTransaction);
        given(transactionToDtoMapper.toDto(foundTransaction)).willReturn(expectedTransactionDto);
        
        //when
        TransactionDto transactionDto = queryingService.getTransaction(transactionId);

        //then
        assertThat(transactionDto, Matchers.is(expectedTransactionDto));
    }
    
    @Test
    public void shouldThrowTransactionNotFoundExceptionIfTransactionBySpecifiedIdNotFound() {
        //given & then
        Long transactionId = 123L;
        given(transactionByIdFinder.getTransactionOrThrowExceptionIfNotFound(eq(transactionId), any())).willAnswer(
                (InvocationOnMock invocation) -> { 
                    throw ((Supplier<RuntimeException>) invocation.getArguments()[1]).get(); 
                }
        );
        thrown.expect(TransactionNotFoundException.class);
        thrown.expectMessage(transactionId.toString());

        //when
        queryingService.getTransaction(transactionId);
    }
    
    @Test
    public void shouldPropagateTransactionIdsSearchByTypeToTransactionRepository() {
        //given
        String transactionType = "cars";
        Collection<Long> expectedIds = Arrays.asList(12L, 34L, 56L);
        given(transactionRepository.getTransactionsIdsByType(transactionType)).willReturn(expectedIds);
        
        //when
        Collection<Long> transactionsIdsByType = queryingService.getTransactionsIdsByType(transactionType);

        //then
        assertThat(transactionsIdsByType, sameInstance(expectedIds));
    }

    @Test
    public void shouldPropagateSpecifiedAndLinkedTransactionsAmountSumCalculationToTransactionRepository() {
        //given
        Long transactionId = 123L;
        Transaction transaction = mock(Transaction.class);
        BigDecimal expectedSum = BigDecimal.valueOf(123.45);
        given(transactionByIdFinder.getTransactionOrThrowExceptionIfNotFound(eq(transactionId), any())).willReturn(transaction);
        given(transactionRepository.getSpecifiedAndLinkedTransactionsAmountSum(transaction)).willReturn(expectedSum);

        //when
        BigDecimal sum = queryingService.getSpecifiedAndLinkedTransactionsAmountSum(transactionId);

        //then
        assertThat(sum, comparesEqualTo(expectedSum));
    }
}