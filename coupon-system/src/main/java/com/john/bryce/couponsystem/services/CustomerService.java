package com.john.bryce.couponsystem.services;

import com.john.bryce.couponsystem.entities.Category;
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

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService extends ClientService {

    public LoginResponse login(String email, String password) throws CouponSystemException {
        Customer customerFromDb = customerRepository.findByEmailAndPassword(email, password).orElseThrow(() -> new CouponSystemException(ErrorMessage.CUSTOMER_LOGIN_ERROR));

        Information information = new Information(customerFromDb.getId(), email, ClientType.CUSTOMER, LocalDateTime.now().plusDays(1));
        UUID token = tokenManager.addToken(information);

        return new LoginResponse(customerFromDb.getId(), token, email, customerFromDb.getFirstName(), ClientType.CUSTOMER);
    }

    @Transactional
    public Coupon purchaseCoupon(UUID token, long couponId) throws CouponSystemException {
        long customerId = tokenManager.validateToken(token, ClientType.CUSTOMER);
        //1. To check if a Coupon is Exist
        Coupon couponFromDb = couponRepository.findById(couponId).orElseThrow(() -> new CouponSystemException(ErrorMessage.NOT_EXIST_COUPON));

        //2. To check if a Customer is existed
        Customer customerFromDb = customerRepository.findById(customerId).orElseThrow(() -> new CouponSystemException(ErrorMessage.NOT_EXIST_CUSTOMER));

        //3. Checking if the customer hasn't purchased the coupon yet
        if (customerFromDb.getCoupons().contains(couponFromDb)) {
            throw new CouponSystemException(ErrorMessage.COUPON_PURCHASED);
        }

        if (couponFromDb.getEndDate().isBefore(Date.valueOf(LocalDate.now()).toLocalDate())) {
            throw new CouponSystemException(ErrorMessage.COUPON_EXPIRED);
        }

        if (couponFromDb.getAmount() <= 0) {
            throw new CouponSystemException(ErrorMessage.COUPON_OUT_OF_STOCK);
        }
        couponFromDb.setAmount(couponFromDb.getAmount() - 1);

        couponRepository.save(couponFromDb);

        couponRepository.addCouponPurchase(customerId, couponId);
        return couponFromDb;
    }

    public List<Coupon> getCustomerCoupons(UUID token) throws CouponSystemException {
        long customerId = tokenManager.validateToken(token, ClientType.CUSTOMER);

        Customer customerFromDb = customerRepository.findById(customerId).orElseThrow(() -> new CouponSystemException(ErrorMessage.NOT_EXIST_CUSTOMER));

        return couponRepository.findByCustomerId(customerFromDb.getId());
    }

    public List<Coupon> getCustomerCouponsByCategory(UUID token, Category category) throws CouponSystemException {
        long customerId = tokenManager.validateToken(token, ClientType.CUSTOMER);

        Customer customerFromDb = customerRepository.findById(customerId).orElseThrow(() -> new CouponSystemException(ErrorMessage.NOT_EXIST_CUSTOMER));

        return couponRepository.findByCustomerIdAndCategory(customerFromDb.getId(), category.name());
    }

    public List<Coupon> getCustomerCouponsByMaxPrice(UUID token, double maxPrice) throws CouponSystemException {
        long customerId = tokenManager.validateToken(token, ClientType.CUSTOMER);

        Customer customerFromDb = customerRepository.findById(customerId).orElseThrow(() -> new CouponSystemException(ErrorMessage.NOT_EXIST_CUSTOMER));

        return couponRepository.getAllCouponsByCustomerIdAndMaxPrice(customerFromDb.getId(), maxPrice);
    }

    public Customer getCustomerDetails(UUID token) throws CouponSystemException {
        long customerId = tokenManager.validateToken(token, ClientType.CUSTOMER);

        return customerRepository.findById(customerId).orElseThrow(() -> new CouponSystemException(ErrorMessage.NOT_EXIST_CUSTOMER));
    }

    public List<Coupon> findAllCouponsNotPurchase(UUID token) throws CouponSystemException {
        long customerId = tokenManager.validateToken(token, ClientType.CUSTOMER);

        return couponRepository.findAllCouponsNotPurchasedByCustomer(customerId);
    }
}
