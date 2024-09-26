package com.john.bryce.couponsystem.clr;

import com.john.bryce.couponsystem.entities.Category;
import com.john.bryce.couponsystem.entities.Coupon;
import com.john.bryce.couponsystem.entities.Customer;
import com.john.bryce.couponsystem.exceptions.CouponSystemException;
import com.john.bryce.couponsystem.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Order(3)
public class CustomerControllerTest implements CommandLineRunner {

    private final RestTemplate restTemplate;
    private final String URL = "http://localhost:8080/api/customer";
    private final CustomerService customerService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n");
        System.out.println("CUSTOMER CONTROLLER TESTS\n");
        String token = generateToken();
        addNewPurchase(token, 2);
        getCustomerCoupons(token);
        getCustomerCouponsByCategory(token, Category.ELECTRICITY);
        getCustomerCouponsByMaxPrice(token, 20);
        getCustomerDetails(token);
    }

    private String generateToken() {
        try {
            return customerService.login("customer3@gmail.com", "1234").getToken().toString();
        } catch (CouponSystemException e) {
            throw new RuntimeException(e);
        }
    }

    public void addNewPurchase(String token, long couponId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            String url = URL + "/purchase/" + couponId;

            ResponseEntity<Void> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    Void.class
            );

            System.out.println("Purchase Coupon Status: " + responseEntity.getStatusCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getCustomerCoupons(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<List<Coupon>> responseEntity = restTemplate.exchange(
                    URL + "/coupons",
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<List<Coupon>>() {
                    }
            );

            List<Coupon> coupons = responseEntity.getBody();
            System.out.println("Customer Coupons: " + coupons + responseEntity.getStatusCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getCustomerCouponsByCategory(String token, Category category) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            String url = URL + "/coupons/category?category=" + category.name();

            ResponseEntity<List<Coupon>> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<List<Coupon>>() {
                    }
            );

            List<Coupon> coupons = responseEntity.getBody();
            System.out.println("Customer Coupons by Category: " + coupons + responseEntity.getStatusCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getCustomerCouponsByMaxPrice(String token, double maxPrice) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            String url = URL + "/coupons/max-price?maxPrice=" + maxPrice;

            ResponseEntity<List<Coupon>> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<List<Coupon>>() {
                    }
            );

            List<Coupon> coupons = responseEntity.getBody();
            System.out.println("Customer Coupons by Max Price: " + coupons + responseEntity.getStatusCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getCustomerDetails(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<Customer> responseEntity = restTemplate.exchange(
                    URL + "/details",
                    HttpMethod.GET,
                    requestEntity,
                    Customer.class
            );

            Customer customer = responseEntity.getBody();
            System.out.println("Customer Details: " + customer + responseEntity.getStatusCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}


