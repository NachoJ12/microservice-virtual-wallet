package com.dh.apicard.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@Document(collection = "Credit Card Movements")
public class CreditCardMovement {
    @Id
    private String id;
    private String cardNumber;
    private MovementType movementType;
    private Amount amount;
    private LocalDateTime operationDate;
    private DebtCollector debtCollector;
    private String description;
    private List<PurchaseDetail> purchaseDetail;
    private State state;
    private BigDecimal walletComission;


    @Data
    @Builder
    public static class Amount{
        private String typeOfCurrency;
        private BigDecimal value;
    }

    @Data
    @Builder
    public static class DebtCollector{
        private String documentType;
        private String documentNumber;
        private String businessName;
    }

    @Data
    @Builder
    public static class PurchaseDetail{
        private String article;
        private Integer quantity;
        private BigDecimal unitaryAmount;
        private BigDecimal subtotalAmount;
    }

    public enum MovementType {
        DEBIT, CREDIT;
    }

    public enum State {
        ACTIVE, BYPASSED;
    }


}



