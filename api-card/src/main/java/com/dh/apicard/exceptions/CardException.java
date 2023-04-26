package com.dh.apicard.exceptions;

public class CardException extends Exception {

    public CardException(MessageError message) {
        super(message.message);
    }
}
