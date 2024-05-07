package com.example.GestionaleRistorante.repository;

import com.example.GestionaleRistorante.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    //@Query(value = "SELECT * FROM customer AS c WHERE c.contactNumber = ?1")
    Optional<Customer> findByContactNumber(String contactNumber);
}
