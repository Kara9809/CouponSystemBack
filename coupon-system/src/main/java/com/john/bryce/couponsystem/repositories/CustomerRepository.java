package com.john.bryce.couponsystem.repositories;

import com.john.bryce.couponsystem.entities.Company;
import com.john.bryce.couponsystem.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    //40% driven query
    Optional<Customer> findByEmailAndPassword(String email, String password);

    boolean existsByEmail(String email);
}
