package com.john.bryce.couponsystem.services;

import com.john.bryce.couponsystem.entities.Company;
import com.john.bryce.couponsystem.entities.Coupon;
import com.john.bryce.couponsystem.entities.Customer;
import com.john.bryce.couponsystem.exceptions.CouponSystemException;
import com.john.bryce.couponsystem.exceptions.ErrorMessage;
import com.john.bryce.couponsystem.security.ClientType;
import com.john.bryce.couponsystem.security.Information;
import com.john.bryce.couponsystem.security.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService extends ClientService {

    public LoginResponse login(String email, String password) throws CouponSystemException {
        if (!("admin@admin.com".equalsIgnoreCase(email) && "admin".equals(password))) {
            throw new CouponSystemException(ErrorMessage.ADMIN_LOGIN_ERROR);
        }

        Information information = new Information(-1, email, ClientType.ADMIN, LocalDateTime.now().plusDays(1));
        UUID token = tokenManager.addToken(information);

        return new LoginResponse(-1, token, email, "Admin", ClientType.ADMIN);
    }

    @Transactional
    public Company addCompany(UUID token, Company company) throws CouponSystemException {
        tokenManager.validateToken(token, ClientType.ADMIN);
        if (companyRepository.existsByName(company.getName())) {
            throw new CouponSystemException(ErrorMessage.COMPANY_NAME_EXIST);
        }
        if (companyRepository.existsByEmail(company.getEmail())) {
            throw new CouponSystemException(ErrorMessage.COMPANY_EMAIL_EXIST);
        }
        if (company.getCoupons() != null && !company.getCoupons().isEmpty()) {
            for (Coupon coupon : company.getCoupons()) {
                coupon.setCompany(company);
            }
        }
       return companyRepository.save(company);
    }

    @Transactional
    public void updateCompany(UUID token, Company company) throws CouponSystemException {
        tokenManager.validateToken(token, ClientType.ADMIN);

        Company companyFromDb = companyRepository.findById(company.getId()).orElseThrow(() -> new CouponSystemException(ErrorMessage.COMPANY_UPDATE_ID));
        if (!companyFromDb.getName().equalsIgnoreCase(company.getName())) {
            throw new CouponSystemException(ErrorMessage.COMPANY_UPDATE_NAME);
        }
        if (companyRepository.existsByEmailAndIdNot(company.getEmail(), company.getId())) {
            throw new CouponSystemException(ErrorMessage.COMPANY_EMAIL_EXIST);
        }

        companyFromDb.setEmail(company.getEmail());
        companyFromDb.setPassword(company.getPassword());

        companyRepository.save(companyFromDb);
    }

    @Transactional
    public void deleteCompany(UUID token, long companyId) throws CouponSystemException {
        tokenManager.validateToken(token, ClientType.ADMIN);
        if (!companyRepository.existsById(companyId)) {
            throw new CouponSystemException(ErrorMessage.NOT_EXIST_COMPANY);
        }
        couponRepository.deletePurchasedCouponsByCompanyId(companyId);
        // Delete the company and coupons because of cascade remove
        companyRepository.deleteById(companyId);
    }

    public List<Company> getAllCompanies(UUID token) throws CouponSystemException {
        tokenManager.validateToken(token, ClientType.ADMIN);
        return companyRepository.findAll();
    }

    public Company getSingleCompany(UUID token, long companyId) throws CouponSystemException {
        tokenManager.validateToken(token, ClientType.ADMIN);
        return companyRepository.findById(companyId).orElseThrow(() -> new CouponSystemException(ErrorMessage.NOT_EXIST_COMPANY));
    }

    @Transactional
    public Customer addCustomer(UUID token, Customer customer) throws CouponSystemException {
        tokenManager.validateToken(token, ClientType.ADMIN);
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new CouponSystemException(ErrorMessage.CUSTOMER_EMAIL_EXIST);
        }
        return customerRepository.save(customer);
    }

    @Transactional
    public void updateCustomer(UUID token, Customer customer) throws CouponSystemException {
        tokenManager.validateToken(token, ClientType.ADMIN);
        Customer customerFromDb = customerRepository.findById(customer.getId()).orElseThrow(() -> new CouponSystemException(ErrorMessage.CUSTOMER_UPDATE_ID));

        customerFromDb.setFirstName(customer.getFirstName());
        customerFromDb.setLastName(customer.getLastName());
        customerFromDb.setEmail(customer.getEmail());
        customerFromDb.setPassword(customer.getPassword());

        customerRepository.save(customerFromDb);
    }

    @Transactional
    public void deleteCustomer(UUID token, long customerId) throws CouponSystemException {
        tokenManager.validateToken(token, ClientType.ADMIN);
        if (!customerRepository.existsById(customerId)) {
            throw new CouponSystemException(ErrorMessage.NOT_EXIST_CUSTOMER);
        }
        couponRepository.deleteAllPurchasesCouponsByCustomerId(customerId);
        customerRepository.deleteById(customerId);
    }

    public List<Customer> getAllCustomers(UUID token) throws CouponSystemException {
        tokenManager.validateToken(token, ClientType.ADMIN);
        return customerRepository.findAll();
    }

    public Customer getSingleCustomer(UUID token, long customerId) throws CouponSystemException {
        tokenManager.validateToken(token, ClientType.ADMIN);
        return customerRepository.findById(customerId).orElseThrow(() -> new CouponSystemException(ErrorMessage.NOT_EXIST_CUSTOMER));
    }
}
