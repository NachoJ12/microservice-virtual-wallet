package com.dh.apicard.services;

import com.dh.apicard.model.CreditCard;
import com.dh.apicard.model.CreditCardMovement;

import java.util.List;

public interface ICardService {
    void createCreditCard(String docType, String docNumber, String currency);
    CreditCard getByDocTypeAndDocNumber(String docType, String docNumber);
    List<CreditCard> getAllCards();

    void debit(CreditCardMovement creditCardMovement);
    void payCreditCard(String cardNumber, String docType, String docNumber);

}
