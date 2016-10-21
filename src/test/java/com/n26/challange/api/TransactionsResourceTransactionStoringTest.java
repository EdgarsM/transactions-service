package com.n26.challange.api;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.*;
import static javax.ws.rs.core.Response.Status.*;
import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;

import org.junit.Test;

import com.n26.challange.application.TransactionDto;

public class TransactionsResourceTransactionStoringTest extends RestAssuredBasedIntegrationTest {

    @Test
    public void shouldStoreTransaction() {
        given().
                contentType(JSON).and().accept(JSON).
                body("{\"amount\": 500, \"type\": \"cars\"}").
        when().
                put("/{transactionId}", 10).
        then().
                statusCode(OK.getStatusCode()).
                body("status", is("ok"));
    }
    
    @Test
    public void shouldStoreTransactionWithReferenceToExistingParentTransaction() {
        long parentTransactionId = transactionPreconditions.givenTransaction(20L);
        given().
                contentType(JSON).and().accept(JSON).
                body(new TransactionDto(BigDecimal.TEN, "insurance", parentTransactionId)).
        when().
                put("/{transactionId}", 11).
        then().
                statusCode(OK.getStatusCode()).
                body("status", is("ok"));    
    }
    
    @Test
    public void shouldNotAcceptTransactionWithoutSpecifiedAmount() {
        given().
                contentType(JSON).and().accept(JSON).
                body(new TransactionDto(null, "insurance")).
        when().
                put("/{transactionId}", 30).
        then().
                statusCode(BAD_REQUEST.getStatusCode());
    }

    @Test
    public void shouldNotAcceptTransactionWithoutSpecifiedType() {
        given().
                contentType(JSON).and().accept(JSON).
                body(new TransactionDto(BigDecimal.TEN, null)).
        when().
                put("/{transactionId}", 31).
        then().
                statusCode(BAD_REQUEST.getStatusCode());
    }

    @Test
    public void shouldNotAcceptTransactionWithReferenceToNotExistingParentTransaction() {
        Long notExistingParentTransactionId = 1L;
        given().
                contentType(JSON).and().accept(JSON).
                body(new TransactionDto(BigDecimal.TEN, "insurance", notExistingParentTransactionId)).
        when().
                put("/{transactionId}", 32).
        then().
                statusCode(NOT_ACCEPTABLE.getStatusCode()).
                body("status", is("error")).
                body("message", containsString(notExistingParentTransactionId.toString()));
    }

    @Test
    public void shouldNotAcceptAlreadyStoredTransactionWithSameId() {
        Long existingTransactionId = transactionPreconditions.givenTransaction(33L);
        given().
                contentType(JSON).and().accept(JSON).
                body(new TransactionDto(BigDecimal.TEN, "insurance")).
        when().
                put("/{transactionId}", existingTransactionId).
        then().
                statusCode(NOT_ACCEPTABLE.getStatusCode()).
                body("status", is("error")).
                body("message", containsString(existingTransactionId.toString()));
    }

    @Test
    public void shouldNotAcceptTransactionWithReferenceToItSelf() {
        Long transactionId = 34L;
        given().
                contentType(JSON).and().accept(JSON).
                body(new TransactionDto(BigDecimal.TEN, "insurance", transactionId)).
        when().
                put("/{transactionId}", transactionId).
        then().
                statusCode(NOT_ACCEPTABLE.getStatusCode()).
                body("status", is("error")).
                body("message", containsString(transactionId.toString()));
    }
}