package com.n26.challange.api;

import static javax.ws.rs.core.Response.Status.*;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.n26.challange.application.TransactionNotFoundException;

@Provider
public class TransactionNotFoundExceptionMapper implements ExceptionMapper<TransactionNotFoundException> {

    public Response toResponse(TransactionNotFoundException ex) {
        return Response.status(NOT_FOUND).entity(new ErrorStatus(ex.getMessage())).build();
    }
}