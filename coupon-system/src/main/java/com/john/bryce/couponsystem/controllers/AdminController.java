package com.john.bryce.couponsystem.controllers;

import com.john.bryce.couponsystem.entities.Company;
import com.john.bryce.couponsystem.entities.Customer;
import com.john.bryce.couponsystem.exceptions.CouponSystemException;
import com.john.bryce.couponsystem.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/company")
    @ResponseStatus(HttpStatus.CREATED)
    public Company addCompany(@RequestHeader("Authorization") UUID token, @RequestBody Company company) throws CouponSystemException {
       return adminService.addCompany(token, company);
    }

    @PutMapping("/company")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCompany(@RequestHeader("Authorization") UUID token, @RequestBody Company company) throws CouponSystemException {
        adminService.updateCompany(token, company);
    }

    @DeleteMapping("/company/{companyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompany(@RequestHeader("Authorization") UUID token, @PathVariable long companyId) throws CouponSystemException {
        adminService.deleteCompany(token, companyId);
    }

    @GetMapping("/companies")
    @ResponseStatus(HttpStatus.OK)
    public List<Company> getAllCompanies(@RequestHeader("Authorization") UUID token) throws CouponSystemException {
        return adminService.getAllCompanies(token);
    }

    @GetMapping("/company/{companyId}")
    @ResponseStatus(HttpStatus.OK)
    public Company getSingleCompany(@RequestHeader("Authorization") UUID token, @PathVariable long companyId) throws CouponSystemException {
        return adminService.getSingleCompany(token, companyId);
    }


    @PostMapping("/customer")
    @ResponseStatus(HttpStatus.CREATED)
    public Customer addCustomer(@RequestHeader("Authorization") UUID token, @RequestBody Customer customer) throws CouponSystemException {
        return adminService.addCustomer(token, customer);
    }

    @PutMapping("/customer")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCustomer(@RequestHeader("Authorization") UUID token, @RequestBody Customer customer) throws CouponSystemException {
        adminService.updateCustomer(token, customer);
    }

    @DeleteMapping("/customer/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@RequestHeader("Authorization") UUID token, @PathVariable long customerId) throws CouponSystemException {
        adminService.deleteCustomer(token, customerId);
    }

    @GetMapping("/customers")
    @ResponseStatus(HttpStatus.OK)
    public List<Customer> getAllCustomers(@RequestHeader("Authorization") UUID token) throws CouponSystemException {
        return adminService.getAllCustomers(token);
    }

    @GetMapping("/customer/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public Customer getSingleCustomer(@RequestHeader("Authorization") UUID token, @PathVariable long customerId) throws CouponSystemException {
        return adminService.getSingleCustomer(token, customerId);
    }
}
