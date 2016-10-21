package com.n26.challange.application;

import java.math.BigDecimal;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.n26.challange.domain.Transaction;
import com.n26.challange.domain.TransactionRepository;

@Service
class TransactionQueryingServiceImpl implements TransactionQueryingService {
    
    private final TransactionRepository transactionRepository;
    
    private final TransactionByIdFinder transactionByIdFinder;
    
    private final TransactionToDtoMapper transactionToDtoMapper;

    @Autowired
    public TransactionQueryingServiceImpl(TransactionRepository transactionRepository,
                                          TransactionByIdFinder transactionByIdFinder,
                                          TransactionToDtoMapper transactionToDtoMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionByIdFinder = transactionByIdFinder;
        this.transactionToDtoMapper = transactionToDtoMapper;
    }

    @Override
    public TransactionDto getTransaction(Long transactionId) {
        Transaction transaction = getExistingTransaction(transactionId);
        return transactionToDtoMapper.toDto(transaction);
    }

    @Override
    public Collection<Long> getTransactionsIdsByType(String transactionType) {
        return transactionRepository.getTransactionsIdsByType(transactionType);
    }

    @Override
    public BigDecimal getSpecifiedAndLinkedTransactionsAmountSum(Long transactionId) {
        Transaction transaction = getExistingTransaction(transactionId);
        return transactionRepository.getSpecifiedAndLinkedTransactionsAmountSum(transaction);
    }
    
    private Transaction getExistingTransaction(Long transactionId) {
        return transactionByIdFinder.getTransactionOrThrowExceptionIfNotFound(transactionId,
                () -> new TransactionNotFoundException(transactionId));   
    }
}