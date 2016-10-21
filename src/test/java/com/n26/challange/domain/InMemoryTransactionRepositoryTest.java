package com.n26.challange.domain;

import static java.math.BigDecimal.*;
import static java.util.stream.Collectors.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.LongStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class InMemoryTransactionRepositoryTest {
    
    InMemoryTransactionRepository repository = new InMemoryTransactionRepository();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldJoinParentChildCollectionOnStore() {
        //given
        Transaction transaction = spy(new Transaction(1L, TEN, "all"));
        
        //when
        repository.store(transaction);
        
        //then
        verify(transaction).joinParentChildCollection();
    }

    @Test
    public void shouldThrowExceptionIfTransactionWithSpecifiedIdAlreadyStored() {
        //given & then
        Long transactionId = 1L;
        storedTransaction(transactionId);
        Transaction transaction = new Transaction(transactionId, TEN, "all");
        thrown.expect(TransactionAlreadyStoredException.class);
        thrown.expectMessage(transactionId.toString());

        //when
        repository.store(transaction);
    }
    
    @Test
    public void shouldReturnTransactionIdsBySpecifiedType() {
        //given
        String type = "cars";
        storedTransaction(1L, TEN, type);
        storedTransaction(2L, TEN, type);
        storedTransaction(3L, TEN, "clothes");
        storedTransaction(4L, TEN, type);

        //when
        Collection<Long> transactionsIds = repository.getTransactionsIdsByType(type);

        //then
        assertThat(transactionsIds, hasItems(1L, 2L, 4L));
        assertThat(transactionsIds, not(hasItem(3L)));
    }

    @Test
    public void shouldReturnSpecifiedAndLinkedTransactionsAmountSum() {
        //given
        Transaction parentTransaction = storedTransaction(1L, TEN, "cars");
        storedTransaction(2L, BigDecimal.valueOf(2), parentTransaction);
        storedTransaction(3L, BigDecimal.valueOf(3), parentTransaction);
        storedTransaction(4L, BigDecimal.valueOf(4), "cars");

        //when
        BigDecimal sum = repository.getSpecifiedAndLinkedTransactionsAmountSum(parentTransaction);

        //then
        assertThat(sum, comparesEqualTo(BigDecimal.valueOf(15)));
    }
    
    @Test
    public void shouldSupportConcurrentTransactionStore() throws InterruptedException {
        //given
        Transaction parentTransaction = storedTransaction(1L, TEN, "cars");
        ExecutorService executorService = executorService(50);
        Collection<Callable<Void>> storeTasks = generateStoreTasks(parentTransaction, 100_000, 2L);
        
        //expect
        executorService.invokeAll(storeTasks);
    }

    private Transaction storedTransaction(Long id) {
        return storedTransaction(id, TEN, "cars");
    }

    private Transaction storedTransaction(Long id, BigDecimal amount, String type) {
        Transaction transaction = new Transaction(id, amount, type);
        repository.store(transaction);
        return transaction;
    }

    private Transaction storedTransaction(Long id, BigDecimal amount, Transaction parentTransaction) {
        Transaction transaction = new Transaction(id, amount, "clothes", parentTransaction);
        repository.store(transaction);
        return transaction;
    }
    
    private ExecutorService executorService(int threads) {
        return Executors.newFixedThreadPool(threads);
    }

    private Collection<Callable<Void>> generateStoreTasks(Transaction parentTransaction, int taskCount, long firstId) {
        String[] types = new String[] {"cars", "clothes", "gadgets"};
        return LongStream.range(firstId, firstId + taskCount)
                .mapToObj(transactionId -> (Callable<Void>) () -> {
                    storeTransaction(transactionId, types[(int) transactionId % types.length], parentTransaction);
                    return null;
                })
                .collect(toList());
    }
    
    private void storeTransaction(Long transactionId, String type, Transaction parentTransaction) {
        repository.store(new Transaction(transactionId, BigDecimal.ONE, type, parentTransaction));    
    }
}