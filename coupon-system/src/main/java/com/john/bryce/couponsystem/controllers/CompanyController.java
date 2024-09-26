package com.john.bryce.couponsystem.controllers;

import com.john.bryce.couponsystem.entities.Category;
import com.john.bryce.couponsystem.entities.Company;
import com.john.bryce.couponsystem.entities.Coupon;
import com.john.bryce.couponsystem.exceptions.CouponSystemException;
import com.john.bryce.couponsystem.services.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping("/coupon")
    @ResponseStatus(HttpStatus.CREATED)
    public Coupon addCoupon(@RequestHeader("Authorization") UUID token, @RequestBody Coupon coupon) throws CouponSystemException {
        return companyService.addCoupon(token, coupon);
    }

    @PutMapping("/coupon")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCoupon(@RequestHeader("Authorization") UUID token, @RequestBody Coupon coupon) throws CouponSystemException {
        companyService.updateCoupon(token, coupon);
    }

    @DeleteMapping("/{couponId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCoupon(@RequestHeader("Authorization") UUID token, @PathVariable long couponId) throws CouponSystemException {
        companyService.deleteCoupon(token, couponId);
    }

    @GetMapping("/coupons")
    @ResponseStatus(HttpStatus.OK)
    public List<Coupon> getCompanyCoupons(@RequestHeader("Authorization") UUID token) throws CouponSystemException {
        return companyService.getCompanyCoupons(token);
    }

    @GetMapping("/coupons/category")
    @ResponseStatus(HttpStatus.OK)
    public List<Coupon> getCompanyCouponsByCategory(
            @RequestHeader("Authorization") UUID token,
            @RequestParam(required = false) Category category) throws CouponSystemException {
        //The required = false makes the parameter optional
        if (category != null) {
            return companyService.getCompanyCouponsByCategory(token, category);
        } else {
            return companyService.getCompanyCoupons(token);
        }
    }

    @GetMapping("/coupons/max-price")
    @ResponseStatus(HttpStatus.OK)
    public List<Coupon> getCompanyCouponsByMaxPrice(
            @RequestHeader("Authorization") UUID token,
            @RequestParam(required = false) Double maxPrice) throws CouponSystemException {
        if (maxPrice != null) {
            return companyService.getCompanyCouponsByMaxPrice(token, maxPrice);
        } else {
            return companyService.getCompanyCoupons(token);
        }
    }

    @GetMapping("/details")
    @ResponseStatus(HttpStatus.OK)
    public Company getCompanyDetails(@RequestHeader("Authorization") UUID token) throws CouponSystemException {
        return companyService.getCompanyDetails(token);
    }
}
