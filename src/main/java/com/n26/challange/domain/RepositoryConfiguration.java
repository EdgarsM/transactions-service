package com.n26.challange.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
class RepositoryConfiguration {

    /**
     * Get not production ready in-memory implementation of transaction repository.
     * @return in-memory repository
     */
    @Profile("!prod")
    @Bean
    TransactionRepository transactionRepository() {
        return new InMemoryTransactionRepository();
    }
}
