package com.john.bryce.couponsystem.services;

import com.john.bryce.couponsystem.exceptions.CouponSystemException;
import com.john.bryce.couponsystem.repositories.CompanyRepository;
import com.john.bryce.couponsystem.repositories.CouponRepository;
import com.john.bryce.couponsystem.repositories.CustomerRepository;
import com.john.bryce.couponsystem.security.LoginResponse;
import com.john.bryce.couponsystem.security.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ClientService {

    @Autowired
    protected CompanyRepository companyRepository;
    @Autowired
    protected CouponRepository couponRepository;
    @Autowired
    protected CustomerRepository customerRepository;
    @Autowired
    protected TokenManager tokenManager;

    public abstract LoginResponse login(String email, String password) throws CouponSystemException;


}
