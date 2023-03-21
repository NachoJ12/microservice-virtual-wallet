package com.dh.apicard.repository;

import com.dh.apicard.model.CreditCard;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CreditCardRepository extends MongoRepository<CreditCard, String> {
    CreditCard findByDocumentTypeAndDocumentNumber(String docType, String docNumber);
    CreditCard findByCardNumber(String cardNumber);
}
