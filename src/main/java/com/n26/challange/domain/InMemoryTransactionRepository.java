package com.n26.challange.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class InMemoryTransactionRepository implements TransactionRepository {
    
    private ConcurrentMap<Long, Transaction> transactionsByIds = new ConcurrentHashMap<>();
    
    private ConcurrentMap<String, Collection<Long>> transactionsIdsByTypes = new ConcurrentHashMap<>();

    @Override
    public Optional<Transaction> getTransaction(long transactionId) {
        return Optional.ofNullable(transactionsByIds.get(transactionId));
    }

    @Override
    public void store(Transaction transaction) {
        boolean newTransaction = transactionsByIds.putIfAbsent(transaction.getId(), transaction) == null;
        if (!newTransaction) {
            throw new TransactionAlreadyStoredException(transaction.getId());
        }

        Collection<Long> existingIds = transactionsIdsByTypes.putIfAbsent(transaction.getType(),
                singleTransactionCollection(transaction));
        if (existingIds != null) {
            existingIds.add(transaction.getId());
        }
        
        transaction.joinParentChildCollection();
    }

    @Override
    public Collection<Long> getTransactionsIdsByType(String transactionType) {
        Collection<Long> ids = transactionsIdsByTypes.get(transactionType);
        return ids == null ? Collections.EMPTY_LIST : Collections.unmodifiableCollection(ids);
    }

    @Override
    public BigDecimal getSpecifiedAndLinkedTransactionsAmountSum(Transaction transaction) {
        // Since in this simple in-memory implementation related transactions are represented as a tree, total
        // amount can be calculated by traversing this tree. In production implementation probably this method would do
        // more than traversing this in memory tree - probably issue some Neo4J query if graph storage would be used.
        return transaction.getAmountIncludedLinkedTransactionAmounts();
    }

    private Collection<Long> singleTransactionCollection(Transaction transaction) {
        Collection<Long> transactionsIds = ConcurrentHashMap.newKeySet();
        transactionsIds.add(transaction.getId());
        return transactionsIds;
    }
}
