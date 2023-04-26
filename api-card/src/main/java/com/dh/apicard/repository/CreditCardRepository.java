package com.dh.apicard.repository;

import com.dh.apicard.model.CreditCard;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CreditCardRepository extends MongoRepository<CreditCard, String> {
    Optional<CreditCard> findByDocumentTypeAndDocumentNumber(String docType, String docNumber);
    CreditCard findByCardNumber(String cardNumber);
}
