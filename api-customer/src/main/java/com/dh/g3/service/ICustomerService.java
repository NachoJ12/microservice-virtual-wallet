package com.dh.g3.service;

import com.dh.g3.DTO.CustomerDTO;
import com.dh.g3.model.Customer;

import java.util.Optional;

public interface ICustomerService {
    Optional<Customer> findByDocument(String doctype, String docnum);
    Customer saveCustomer(Customer customer);
    String deleteCustomer(Long customerId);
    Customer modifyCustomer(CustomerDTO customerDTO);
}
