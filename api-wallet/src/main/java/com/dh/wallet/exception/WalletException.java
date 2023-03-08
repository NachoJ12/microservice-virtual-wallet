package com.dh.wallet.exception;

public class WalletException extends Exception {

    public WalletException(MessageError messageError) {
        super(messageError.message);
    }
}
