package com.dh.apicard.services;

import com.dh.apicard.controller.CreditCardController;
import com.dh.apicard.exceptions.CardException;
import com.dh.apicard.model.CreditCard;
import com.dh.apicard.model.CreditCardMovement;

import java.util.List;

public interface ICardService {
    void createCreditCard(String docType, String docNumber, String currency) throws CardException;
    CreditCard getByDocTypeAndDocNumber(String docType, String docNumber) throws CardException;
    List<CreditCard> getAllCards();

    void debit(CreditCardMovement creditCardMovement) throws CardException;
    void payCreditCard(CreditCardController.PayCreditCardDto payCreditCardDto) throws CardException;

}
