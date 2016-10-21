package com.n26.challange.api;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.*;
import static javax.ws.rs.core.Response.Status.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

import io.restassured.filter.log.ResponseLoggingFilter;

public class TransactionsResourceTransactionByIdRetrievalTest extends RestAssuredBasedIntegrationTest {

    @Test
    public void shouldReturnTransactionWithoutParent() {
        long transactionId = transactionPreconditions.givenTransaction(40L, BigDecimal.valueOf(12.34), "meal");
        given().
                accept(JSON).
        when().
                get("/{transactionId}", transactionId).
        then().
                statusCode(OK.getStatusCode()).
                body("amount", comparesEqualTo(BigDecimal.valueOf(12.34))).
                body("type", is("meal")).
                body("parent_id", isEmptyOrNullString());
    }

    @Test
    public void shouldReturnTransactionWithParent() {
        long parentTransactionId = transactionPreconditions.givenTransaction(41L, BigDecimal.ONE, "meal");
        long transactionId = transactionPreconditions.givenTransaction(42L, BigDecimal.valueOf(12.34), "meal", parentTransactionId);
        Long parentId = 
        given().
                accept(JSON).
                filter(new ResponseLoggingFilter()).
        when().
                get("/{transactionId}", transactionId).
        then().
                statusCode(OK.getStatusCode()).
                body("amount", comparesEqualTo(BigDecimal.valueOf(12.34))).
                body("type", is("meal")).
                body("parent_id", notNullValue()).
        extract().
                jsonPath().getLong("parent_id");
        assertThat(parentId, is(parentTransactionId));
    }
    
    @Test
    public void shouldReturnHttpStatusNotFoundIfNotExistingTransactionRequested() {
        Long notExistingTransactionId = 43L;
        given().
                accept(JSON).
        when().
                get("/{transactionId}", notExistingTransactionId).
        then().
                statusCode(NOT_FOUND.getStatusCode()).
                body("status", is("error")).
                body("message", containsString(notExistingTransactionId.toString()));
                
    }
}
