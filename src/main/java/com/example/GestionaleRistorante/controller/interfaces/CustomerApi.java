package com.example.GestionaleRistorante.controller.interfaces;


import com.example.GestionaleRistorante.dto.CustomerDto;
import com.example.GestionaleRistorante.entity.Customer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/customer")
public interface CustomerApi {

    @GetMapping("/{customerId}")
    ResponseEntity<Customer> getCustomerById(@PathVariable Long customerId); //permette di avere una risposta di tipo customer con la risposta contenente il codice http, es 200, 404, etc

    @GetMapping()
    ResponseEntity<List<Customer>> getAllCustomers();

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto);

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer);

    @DeleteMapping("/{customerId}")
    ResponseEntity<String> deleteCustomer(@PathVariable String customerId);
}
