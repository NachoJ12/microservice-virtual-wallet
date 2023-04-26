package com.dh.apicard.exceptions;

public enum MessageError {
    CUSTOMER_WITH_CARD("Customer cannot have more than one card"),
    CUSTOMER_NOT_HAVE_CARD("The customer does not have a registered credit card"),
    CUSTOMER_NOT_HAVE_FUNDS("Customer does not have necessary funds"),
    EXCEEDS_AVAILABLE_LIMIT("The amount exceeds the available limit"),
    CARD_NOT_MATCH("The card does not match the document entered");


    String message;


    MessageError(String message) {
        this.message=message;
    }
}
