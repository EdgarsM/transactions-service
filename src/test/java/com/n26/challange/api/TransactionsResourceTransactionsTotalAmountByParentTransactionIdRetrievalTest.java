package com.n26.challange.api;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.*;
import static javax.ws.rs.core.Response.Status.*;
import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;

import org.junit.Test;

public class TransactionsResourceTransactionsTotalAmountByParentTransactionIdRetrievalTest extends RestAssuredBasedIntegrationTest {
    
    @Test
    public void shouldReturnTotalAmountSpecifiedTransactionAndAllTransitivelyRelatedTransactions() {
        transactionPreconditions.givenTransaction(68L, BigDecimal.TEN, "sibling");
        
        Long grandParentId = transactionPreconditions.givenTransaction(62L, BigDecimal.ONE, "food");
        Long firstParentId = transactionPreconditions.givenTransaction(63L, BigDecimal.TEN, "drink", grandParentId);
        transactionPreconditions.givenTransaction(64L, BigDecimal.valueOf(10.25), "food", grandParentId);
        transactionPreconditions.givenTransaction(65L, BigDecimal.valueOf(1.25), "drink", firstParentId);
        transactionPreconditions.givenTransaction(66L, BigDecimal.valueOf(1.25), "drink", firstParentId);
        transactionPreconditions.givenTransaction(67L, BigDecimal.valueOf(1.25), "drink", firstParentId);
        given().
                accept(JSON).
        when().
                get("/sum/{transactionId}", grandParentId).
        then().
                statusCode(OK.getStatusCode()).
                body("sum", comparesEqualTo(BigDecimal.valueOf(25)));
    }

    @Test
    public void shouldReturnHttpStatusNotFoundIfParentTransactionNotFoundBySpecifiedId() {
        Long notExistingTransactionId = 61L;
        given().
                accept(JSON).
        when().
                get("/sum/{transactionId}", notExistingTransactionId).
        then().
                statusCode(NOT_FOUND.getStatusCode()).
                body("status", is("error")).
                body("message", containsString(notExistingTransactionId.toString()));
    }
}
