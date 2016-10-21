package com.n26.challange.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.n26.challange.domain.Transaction;
import com.n26.challange.domain.TransactionBuilder;
import com.n26.challange.domain.TransactionRepository;

@Service
class TransactionStoringServiceImpl implements TransactionStoringService {

    private final TransactionRepository transactionRepository;
    
    private final TransactionByIdFinder finder;

    @Autowired
    TransactionStoringServiceImpl(TransactionRepository transactionRepository, TransactionByIdFinder finder) {
        this.transactionRepository = transactionRepository;
        this.finder = finder;
    }
    
    @Override
    public void storeTransaction(Long transactionId, TransactionDto transactionDto) {
        Transaction parentTransaction = transactionDto.getOptionalParentId()
                .map(this::getParentTransactionOrThrowExceptionIfNotFound).orElse(null);

        Transaction transaction = TransactionBuilder.get()
                .withId(transactionId)
                .withAmount(transactionDto.getAmount())
                .withType(transactionDto.getType())
                .withParentTransaction(parentTransaction)
                .build();
        transactionRepository.store(transaction);
    }

    private Transaction getParentTransactionOrThrowExceptionIfNotFound(long parentId) {
        return finder.getTransactionOrThrowExceptionIfNotFound(parentId, () -> new ParentTransactionNotFoundException(parentId));
    }
}
