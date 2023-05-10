package com.dh.g3.controller;


import com.dh.g3.DTO.CustomerDTO;
import com.dh.g3.event.CreatedCustomerEventProducer;
import com.dh.g3.model.Customer;
import com.dh.g3.service.impl.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    public CustomerServiceImpl customerService;

    private final CreatedCustomerEventProducer createdCustomerEventProducer;

    public CustomerController(CreatedCustomerEventProducer createdCustomerEventProducer) {
        this.createdCustomerEventProducer = createdCustomerEventProducer;
    }

    @GetMapping("/{doctype}/{docnum}")
    public Optional<Customer> getCustomer (@PathVariable String doctype, @PathVariable String docnum){
        return customerService.findByDocument(doctype, docnum);
    }

    @PostMapping("/save")
    public ResponseEntity<Object> saveCustomer (@RequestBody Customer customer){
        customerService.saveCustomer(customer);
        createdCustomerEventProducer.publishCreatedCustomerEvent(new CreatedCustomerEventProducer.Data(customer.getDocumentType(), customer.getDocumentNumber()));
        return ResponseEntity.ok("Customer save");
    }


    @PutMapping("/update")
    public Customer updateCustomer (@RequestBody CustomerDTO customerDTO){
        return customerService.modifyCustomer(customerDTO);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteCustomer (@PathVariable Long id){
        customerService.deleteCustomer(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Customer " + id + " delete");
    }



}
