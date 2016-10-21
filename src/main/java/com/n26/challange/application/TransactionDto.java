package com.n26.challange.application;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;

import java.math.BigDecimal;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(NON_NULL)
public class TransactionDto {
    
    @NotNull
    private final BigDecimal amount;
    
    @NotNull
    @Size(min = 1)
    private final String type;

    @JsonProperty("parent_id")
    private final Long parentId;
    
    public TransactionDto(BigDecimal amount, String type) {
        this(amount, type, null);
    }

    @JsonCreator
    public TransactionDto(@JsonProperty("amount") BigDecimal amount, @JsonProperty("type") String type, 
                          @JsonProperty("parent_id") Long parentId) {
        this.amount = amount;
        this.type = type;
        this.parentId = parentId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    
    public String getType() {
        return type;
    }

    public Long getParentId() {
        return parentId;
    }
    
    @JsonIgnore
    public Optional<Long> getOptionalParentId() {
        return Optional.ofNullable(parentId);
    }
}
