package com.n26.challange.application;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TransactionDtoTest {
    
    ObjectMapper objectMapper = new ObjectMapper();

    StringWriter json = new StringWriter();

    @Test
    public void shouldSerialiseTransactionDtoIntoCorrectJsonRepresentation() throws IOException {
        //given
        TransactionDto transactionDto = new TransactionDto(BigDecimal.valueOf(12.34), "insurance");
        
        //when
        objectMapper.writeValue(json, transactionDto);
        
        //then
        assertThat(json.toString(), equalTo("{\"amount\":12.34,\"type\":\"insurance\"}"));
    }

    @Test
    public void shouldSerialiseTransactionDtoWithParentIdIntoCorrectJsonRepresentation() throws IOException {
        //given
        TransactionDto transactionDto = new TransactionDto(BigDecimal.valueOf(12.34), "insurance", 123L);

        //when
        objectMapper.writeValue(json, transactionDto);

        //then
        assertThat(json.toString(), equalTo("{\"amount\":12.34,\"type\":\"insurance\",\"parent_id\":123}"));
    }

    @Test
    public void shouldDeSerialiseJsonRepresentationIntoTransactionDto() throws IOException {
        //given
        String json = "{\"amount\":12.34,\"type\":\"insurance\"}";

        //when
        TransactionDto transactionDto = objectMapper.readValue(json, TransactionDto.class);

        //then
        assertThat(transactionDto.getAmount(), comparesEqualTo(BigDecimal.valueOf(12.34)));
        assertThat(transactionDto.getType(), is("insurance"));
        assertThat(transactionDto.getParentId(), nullValue());
    }

    @Test
    public void shouldDeSerialiseJsonRepresentationIntoTransactionDtoWithParentId() throws IOException {
        //given
        String json = "{\"amount\":12.34,\"type\":\"insurance\",\"parent_id\":123}";

        //when
        TransactionDto transactionDto = objectMapper.readValue(json, TransactionDto.class);

        //then
        assertThat(transactionDto.getAmount(), comparesEqualTo(BigDecimal.valueOf(12.34)));
        assertThat(transactionDto.getType(), is("insurance"));
        assertThat(transactionDto.getParentId(), equalTo(123L));
    }
}