package com.n26.challange.api;

import static javax.ws.rs.core.Response.Status.*;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.n26.challange.application.ParentTransactionNotFoundException;

@Provider
public class ParentTransactionNotFoundExceptionMapper implements ExceptionMapper<ParentTransactionNotFoundException> {

    @Override
    public Response toResponse(ParentTransactionNotFoundException ex) {
        return Response.status(NOT_ACCEPTABLE).entity(new ErrorStatus(ex.getMessage())).build();
    }
}
