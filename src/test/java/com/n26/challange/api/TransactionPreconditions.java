package com.n26.challange.api;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.*;
import static javax.ws.rs.core.Response.Status.*;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.n26.challange.application.TransactionDto;

@Component
class TransactionPreconditions {

    public Long givenTransaction(long transactionId) {
        return givenTransaction(transactionId, BigDecimal.valueOf(500.42), "cars", null);
    }

    public Long givenTransaction(long transactionId, BigDecimal amount, String type) {
        return givenTransaction(transactionId, amount, type, null);
    }
    
    public Long givenTransaction(long transactionId, BigDecimal amount, String type, Long parentId) {
        given().
                contentType(JSON).and().accept(JSON).
                body(new TransactionDto(amount, type, parentId)).
        when().
                put("/{transactionId}", transactionId).
        then().
                statusCode(OK.getStatusCode()); 
        return transactionId;
    }
}
