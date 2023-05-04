package com.dh.apicard.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter
@Setter
@Document(collection = "Credit Card")
@Builder
public class CreditCard {
    @Id
    private String cardNumber;
    private String documentType;
    private String documentNumber;
    private String currency;
    private BigDecimal qualifiedLimit;
    private BigDecimal consumedLimit;
    private BigDecimal availableLimit;


}
