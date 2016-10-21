package com.n26.challange.api;

import static javax.ws.rs.core.MediaType.*;

import java.util.Collection;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.n26.challange.application.TransactionDto;
import com.n26.challange.application.TransactionQueryingService;
import com.n26.challange.application.TransactionStoringService;

@Component
@Path(value = TransactionsResource.RESOURCE_PATH)
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class TransactionsResource {
    
    static final String RESOURCE_PATH = "/transaction";
    
    private final TransactionStoringService transactionStoringService;
    
    private final TransactionQueryingService transactionQueryingService;

    @Autowired
    TransactionsResource(TransactionStoringService transactionStoringService,
                         TransactionQueryingService transactionQueryingService) {
        this.transactionStoringService = transactionStoringService;
        this.transactionQueryingService = transactionQueryingService;
    }

    @PUT
    @Path("/{transactionId}")
    public Status storeTransaction(@PathParam("transactionId") Long transactionId, @Valid TransactionDto transaction) {
        transactionStoringService.storeTransaction(transactionId, transaction);   
        return new Status("ok");
    }

    @GET
    @Path("/{transactionId}")
    public TransactionDto getTransaction(@PathParam("transactionId") Long transactionId) {
        return transactionQueryingService.getTransaction(transactionId);
    }

    @GET 
    @Path("/types/{type}")
    public Collection<Long> getTransactionsIdsByType(@PathParam("type") String transactionType) {
        return transactionQueryingService.getTransactionsIdsByType(transactionType);    
    }

    @GET
    @Path("/sum/{transactionId}")
    public TransactionsAmountSum getSpecifiedAndLinkedTransactionsAmountSum(@PathParam("transactionId") Long transactionId) {
        return new TransactionsAmountSum(transactionQueryingService.getSpecifiedAndLinkedTransactionsAmountSum(transactionId));    
    }
}