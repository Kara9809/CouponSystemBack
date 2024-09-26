package com.john.bryce.couponsystem.repositories;

import com.john.bryce.couponsystem.entities.Company;
import com.john.bryce.couponsystem.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    //40% Driven query

    boolean existsByName(String name);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, long id);

    Optional<Company> findByEmailAndPassword(String email, String password);
}
