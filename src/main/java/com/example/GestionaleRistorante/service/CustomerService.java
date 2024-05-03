package com.example.GestionaleRistorante.service;


import com.example.GestionaleRistorante.dto.CustomerDto;
import com.example.GestionaleRistorante.entity.Customer;
import com.example.GestionaleRistorante.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    //l'entità dipendente Reservation invece per semplicità la assemblo dentro il service
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

    public Customer updateCustomer (Customer customerInput){
       // public Customer updateCustomer (Long id, String customerSurname, String customerName, String contactNumber, String email, String address, Boolean isPremium, Reservation reservation){
        Optional<Customer> customerOptional =  customerRepository.findById(customerInput.getId());
        if(customerOptional.isPresent()){
            Customer customer = customerOptional.get();
            if(!Objects.equals(customer.getCustomerName(), customerInput.getCustomerName())){
                customer.setCustomerName(customerInput.getCustomerName());
            }
            if(!Objects.equals(customer.getCustomerSurname(), customerInput.getCustomerSurname())){
                customer.setCustomerSurname(customer.getCustomerSurname());
            }
            if(!Objects.equals(customer.getContactNumber(), customerInput.getContactNumber())){
                customer.setContactNumber(customerInput.getContactNumber());
            }
            if(!Objects.equals(customer.getEmail(), customerInput.getEmail())){
                customer.setEmail(customerInput.getEmail());
            }
            if(!Objects.equals(customer.getAddress(), customerInput.getAddress())){
                customer.setAddress(customerInput.getAddress());
            }
            if(customer.getIsPremium()!=customerInput.getIsPremium()){
                customer.setIsPremium(customerInput.getIsPremium());
            }
          return customerRepository.save(customer);
        }   else{
                log.error("Table do not exist");
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
}
