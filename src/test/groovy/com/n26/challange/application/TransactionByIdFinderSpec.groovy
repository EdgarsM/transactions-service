package com.n26.challange.application

import com.n26.challange.domain.Transaction
import com.n26.challange.domain.TransactionRepository
import spock.lang.Specification

import static java.util.Optional.empty

/**
 * Groovy/Spock specification showing some advantages of this language/framework in BDD style test development. 
 * I didn't use Groovy/Spock for all tests because it was not clear from this challenge requirements if it's allowed
 * to use other JVM language for testing purposes. I also didn't receive confirmation to this question from Karo.
 */
class TransactionByIdFinderSpec extends Specification {
    
    def transactionRepository = Mock(TransactionRepository)
    
    def transactionByIdFinder = new TransactionByIdFinder(transactionRepository)

    def "should return transaction if found by specified id"() {
        given:
            def transactionId = 123L
            def expectedTransaction = Mock(Transaction)
            transactionRepository.getTransaction(transactionId) >> Optional.of(expectedTransaction)
        
        when:
            def foundTransaction =  transactionByIdFinder.getTransactionOrThrowExceptionIfNotFound(transactionId, {
                new RuntimeException("Should not be called")    
            })
        
        then:
            foundTransaction.is(expectedTransaction)
    }

    def "should throw supplied exception if transaction not found by specified id"() {
        given:
            def transactionId = 123L
            transactionRepository.getTransaction(transactionId) >> empty()
        
        when:
            transactionByIdFinder.getTransactionOrThrowExceptionIfNotFound(transactionId, {
                new TransactionNotFoundException(transactionId)
            })
        
        then:
            def thrown = thrown(TransactionNotFoundException)
            thrown.message.contains("$transactionId")
    }
}
