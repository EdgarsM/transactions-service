package com.n26.challange.api;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.*;
import static java.math.BigDecimal.*;
import static javax.ws.rs.core.Response.Status.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class TransactionsResourceTransactionsIdsByTypeRetrievalTest extends RestAssuredBasedIntegrationTest {

    @Test
    public void shouldReturnArrayOfTransactionIdsFoundBySpecifiedType() {
        String matchingType = "existingType1";
        Long matchingParentId = transactionPreconditions.givenTransaction(50L, ONE, matchingType);
        Long matchingChildId = transactionPreconditions.givenTransaction(51L, ONE, matchingType, matchingParentId);
        Long notMatchingChildId = transactionPreconditions.givenTransaction(52L, ONE, "existingType2", matchingParentId);
        Long matchingSiblingId = transactionPreconditions.givenTransaction(53L, ONE, matchingType);
        given().
                accept(JSON).
        when().
                get("/types/{type}", matchingType).
        then().
                statusCode(OK.getStatusCode()).
                body("", hasItems(matchingParentId.intValue(), matchingChildId.intValue(), matchingSiblingId.intValue())).
                body("", not(hasItem(notMatchingChildId.intValue())));
    }

    @Test
    public void shouldReturnEmptyArrayOfTransactionIdsIfNoTransactionsFoundBySpecifiedType() {
        transactionPreconditions.givenTransaction(58L, ONE, "existingType3");
        given().
                accept(JSON).
        when().
                get("/types/{type}", "notExistingType").
        then().
                statusCode(OK.getStatusCode()).
                body("", emptyCollectionOf(Long.class));
    }
}
