package com.example.GestionaleRistorante.service;


import com.example.GestionaleRistorante.dto.CustomerDto;
import com.example.GestionaleRistorante.entity.Customer;
import com.example.GestionaleRistorante.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.util.*;

@Slf4j
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }


    //CRUD OPERATIONS

    //le entità indipendenti le costruisco fuori dai metodi crud e gliele passo come dipendenze, così non rinuncio alla flessibilità data dal pattern builder
    //l'entità Reservation invece per semplicità la assemblo dentro il service
    public CustomerDto addCustomer (CustomerDto customerDto){
        try{
            Optional<Customer> customerOptional = customerRepository.findByContactNumber(customerDto.getContactNumber());
            if(customerOptional.isPresent()){
                log.info(String.format("Customer %s %s already exist", customerOptional.get().getCustomerName(), customerOptional.get().getCustomerSurname()));
                customerDto = modelMapper.map(customerOptional.get(), CustomerDto.class);
            } else{
                customerRepository.save(modelMapper.map(customerDto, Customer.class));
            }
            return customerDto;
        } catch(Exception e){
            log.error("Error during adding customer process occurred");
            return null;
          }

    }

    public Customer updateCustomer (Customer customerInput){ //permette di mandare json con campi nulli
        try{
            Customer customer = customerRepository.findById(customerInput.getId())
                                                  .orElseThrow(()->new Exception("Customer not found"));
            BeanUtils.copyProperties(customerInput, customer, getNullPropertyNames(customerInput));
            return customerRepository.save(customer);
        } catch(Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    public void deleteCustomer(Long id){
        customerRepository.deleteById(id);
    }

    public List<Customer> getCustomers(){
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomer(Long id){
        return customerRepository.findById(id);
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<String>();
        for(PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

}
