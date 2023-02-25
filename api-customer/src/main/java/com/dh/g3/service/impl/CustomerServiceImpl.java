package com.dh.g3.service.impl;

import com.dh.g3.DTO.CustomerDTO;
import com.dh.g3.model.Customer;
import com.dh.g3.repository.ICustomer;
import com.dh.g3.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private ICustomer iCustomer;

    @Override
    public Optional<Customer> findByDocument(String doctype, String docnum) {
        return iCustomer.findByDocument(doctype, docnum);
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        return iCustomer.save(customer);
    }

    @Override
    public String deleteCustomer(Long customerId) {
        iCustomer.deleteById(customerId);
        return "Se ha eliminado con éxito";
    }

    @Override
    public Customer modifyCustomer(CustomerDTO customerDTO) {
        Optional<Customer> customer = iCustomer.findById(customerDTO.getId());
        customer.get().setBirthDate(customerDTO.getBirthDate());
        customer.get().setName(customerDTO.getName());
        customer.get().setSurname(customerDTO.getSurname());
        customer.get().setGender(customerDTO.getGender());
        return iCustomer.save(customer.get());
    }
}
