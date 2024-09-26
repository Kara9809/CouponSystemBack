package com.john.bryce.couponsystem.controllers;


import com.john.bryce.couponsystem.entities.Category;
import com.john.bryce.couponsystem.entities.Coupon;
import com.john.bryce.couponsystem.entities.Customer;
import com.john.bryce.couponsystem.exceptions.CouponSystemException;
import com.john.bryce.couponsystem.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/purchase/{couponId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Coupon purchaseCoupon(@RequestHeader("Authorization") UUID token, @PathVariable long couponId) throws CouponSystemException {
        return customerService.purchaseCoupon(token, couponId);
    }

    @GetMapping("/coupons")
    @ResponseStatus(HttpStatus.OK)
    public List<Coupon> getCustomerCoupons(@RequestHeader("Authorization") UUID token) throws CouponSystemException {
        return customerService.getCustomerCoupons(token);
    }

    @GetMapping("/coupons/category")
    @ResponseStatus(HttpStatus.OK)
    public List<Coupon> getCustomerCouponsByCategory(@RequestHeader("Authorization") UUID token, @RequestParam Category category) throws CouponSystemException {
        return customerService.getCustomerCouponsByCategory(token, category);
    }

    @GetMapping("/coupons/max-price")
    @ResponseStatus(HttpStatus.OK)
    public List<Coupon> getCustomerCouponsByMaxPrice(@RequestHeader("Authorization") UUID token, @RequestParam double maxPrice) throws CouponSystemException {
        return customerService.getCustomerCouponsByMaxPrice(token, maxPrice);
    }

    @GetMapping("/details")
    @ResponseStatus(HttpStatus.OK)
    public Customer getCustomerDetails(@RequestHeader("Authorization") UUID token) throws CouponSystemException {
        return customerService.getCustomerDetails(token);
    }

    @GetMapping("/all-coupons")
    @ResponseStatus(HttpStatus.OK)
    public List<Coupon> getAllCoupons(@RequestHeader("Authorization") UUID token) throws CouponSystemException {
        return customerService.findAllCouponsNotPurchase(token);
    }

}
