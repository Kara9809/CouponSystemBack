package com.john.bryce.couponsystem.clr;

import com.john.bryce.couponsystem.controllers.AdminController;
import com.john.bryce.couponsystem.entities.Category;
import com.john.bryce.couponsystem.entities.Company;
import com.john.bryce.couponsystem.entities.Coupon;
import com.john.bryce.couponsystem.exceptions.CouponSystemException;
import com.john.bryce.couponsystem.services.AdminService;
import com.john.bryce.couponsystem.services.CompanyService;
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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Order(2)
public class CompanyControllerTest implements CommandLineRunner {

    private final RestTemplate restTemplate;
    private final String URL = "http://localhost:8080/api/company";
    private final CompanyService companyService;


    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n");
        System.out.println("COMPANY CONTROLLER TESTS\n");
        String token = generateToken();
        addNewCoupon(token);
        updateCoupon(token);
        deleteCoupon(token);
        getCompanyCoupons(token);
        getCompanyCouponsByCategory(token, Category.FOOD);
        getCompanyCouponsByMaxPrice(token, 30.00);
        getCompanyDetails(token);
    }

    private String generateToken() {
        try {
            return companyService.login("company3@gmail.com", "1234").getToken().toString();
        } catch (CouponSystemException e) {
            throw new RuntimeException(e);
        }
    }

    public void addNewCoupon(String token) {
        try {
            Company companyFromDb = companyService.getCompanyDetails(UUID.fromString(token));
            Coupon coupon1 = new Coupon(0, companyFromDb, Category.FOOD, "tasty", "description1", LocalDate.now(), LocalDate.now().plusDays(30), 10, 15.00, "https://picsum.photos/200/300?hmac=" + Math.random() * 9_999);
            Coupon coupon2 = new Coupon(0, companyFromDb, Category.ELECTRICITY, "good", "description2", LocalDate.now(), LocalDate.now().plusDays(25), 15, 10.00, "https://picsum.photos/200/300?hmac=" + Math.random() * 9_999);
            Coupon coupon3 = new Coupon(0, companyFromDb, Category.RESTAURANT, "no tasty", "description3", LocalDate.now(), LocalDate.now().plusDays(15), 5, 11.00, "https://picsum.photos/200/300?hmac=" + Math.random() * 9_999);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Coupon> requestEntity1 = new HttpEntity<>(coupon1, headers);
            HttpEntity<Coupon> requestEntity2 = new HttpEntity<>(coupon2, headers);
            HttpEntity<Coupon> requestEntity3 = new HttpEntity<>(coupon3, headers);

            ResponseEntity<Void> responseEntity1 = restTemplate.exchange(URL + "/coupon", HttpMethod.POST, requestEntity1, Void.class);
            ResponseEntity<Void> responseEntity2 = restTemplate.exchange(URL + "/coupon", HttpMethod.POST, requestEntity2, Void.class);
            ResponseEntity<Void> responseEntity3 = restTemplate.exchange(URL + "/coupon", HttpMethod.POST, requestEntity3, Void.class);

            System.out.println("Create coupon successfully " + responseEntity1.getStatusCode());
            System.out.println("Create coupon successfully " + responseEntity2.getStatusCode());
            System.out.println("Create coupon successfully " + responseEntity3.getStatusCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateCoupon(String token) {
        try {
            Company companyFromDb = companyService.getCompanyDetails(UUID.fromString(token));
            Coupon coupon = new Coupon(3, companyFromDb, Category.RESTAURANT, "very tasty", "description3", LocalDate.now(), LocalDate.now().plusDays(15), 5, 11.00, "https://picsum.photos/200/300?hmac=" + Math.random() * 9_999);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Coupon> requestEntity1 = new HttpEntity<>(coupon, headers);
            ResponseEntity<Void> responseEntity = restTemplate.exchange(URL + "/coupon", HttpMethod.PUT, requestEntity1, Void.class);
            System.out.println("Update coupon successfully " + responseEntity.getStatusCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteCoupon(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<Void> responseEntity = restTemplate.exchange(URL + "{couponId}", HttpMethod.DELETE, requestEntity, Void.class, 1);
            System.out.println("Delete coupon successfully " + responseEntity.getStatusCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getCompanyCoupons(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<List<Coupon>> responseEntity = restTemplate.exchange(
                    URL + "/coupons/category",
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<List<Coupon>>() {
                    }
            );

            List<Coupon> coupons = responseEntity.getBody();
            System.out.println("All Coupons successfully " + responseEntity.getStatusCode());
            System.out.println(coupons);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getCompanyCouponsByCategory(String token, Category category) {
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

            List<Coupon> couponsByCategory = responseEntity.getBody();
            System.out.println("Company Coupons by Category: " + couponsByCategory + responseEntity.getStatusCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getCompanyCouponsByMaxPrice(String token, double maxPrice) {
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

            List<Coupon> couponsByMaxPrice = responseEntity.getBody();
            System.out.println("Company Coupons by Max Price: " + couponsByMaxPrice + responseEntity.getStatusCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getCompanyDetails(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<Company> responseEntity = restTemplate.exchange(
                    URL + "/details",
                    HttpMethod.GET,
                    requestEntity,
                    Company.class
            );

            Company company = responseEntity.getBody();
            System.out.println("Company Details: " + company + responseEntity.getStatusCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
