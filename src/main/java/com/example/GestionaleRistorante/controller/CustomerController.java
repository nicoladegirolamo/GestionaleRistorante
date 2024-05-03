package com.example.GestionaleRistorante.controller;

import com.example.GestionaleRistorante.controller.interfaces.CustomerApi;
import com.example.GestionaleRistorante.dto.CustomerDto;
import com.example.GestionaleRistorante.entity.Customer;
import com.example.GestionaleRistorante.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class CustomerController implements CustomerApi {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public ResponseEntity<Customer> getCustomerById(Long customerId) {
        try {
            Optional<Customer> customerOptional = customerService.getCustomer(customerId);
            return new ResponseEntity<>(customerOptional.get(), HttpStatus.OK);
        }   catch(Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    @Override
    public ResponseEntity<List<Customer>> getAllCustomers() {
        try {
            List<Customer> customerList = customerService.getCustomers();
            return new ResponseEntity<>(customerList, HttpStatus.OK);
        }   catch (Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    @Override
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto) {
        try {
            CustomerDto outCustomerDto = customerService.addCustomer(customerDto);
            return new ResponseEntity<>(outCustomerDto, HttpStatus.OK);
        }   catch(Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    @Override
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer) {
        try {
            Customer outCustomer = customerService.updateCustomer(customer);
            return new ResponseEntity<>(outCustomer, HttpStatus.OK);
        }   catch(Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    @Override
    public ResponseEntity<String> deleteCustomer(String customerId) {
        try {
            customerService.deleteCustomer(Long.getLong(customerId));
            return new ResponseEntity<>("Deleted",HttpStatus.OK);
        }   catch(Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

}
