package com.dh.g3.repository;

import com.dh.g3.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICustomer extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c WHERE c.documentType= ?1 AND c.documentNumber = ?2")
    Optional<Customer> findByDocument(String doctype, String docnum);
}
