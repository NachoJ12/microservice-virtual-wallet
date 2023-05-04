package com.dh.apicard.repository;

import com.dh.apicard.model.CreditCardMovement;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CreditCardMovementRepository extends MongoRepository<CreditCardMovement, String> {
}
