package com.john.bryce.couponsystem.clr;

import com.john.bryce.couponsystem.entities.Company;
import com.john.bryce.couponsystem.entities.Customer;
import com.john.bryce.couponsystem.exceptions.CouponSystemException;
import com.john.bryce.couponsystem.services.AdminService;
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
@Order(1)
public class AdminControllerTest implements CommandLineRunner {

    private final RestTemplate restTemplate;
    private final String URL = "http://localhost:8080/api/admin";
    private final AdminService adminService;

    @Override
    public void run(String... args) throws Exception {
        String token = generateToken();
        addNewCompany(token);
        updateCompany(token);
        deleteCompany(token);
        getAllCompanies(token);
        getSingleCompany(token);
        addNewCustomer(token);
        updateCustomer(token);
        deleteCustomer(token);
        getAllCustomers(token);
        getSingleCustomer(token);
    }

    public String generateToken() {
        try {
            return adminService.login("admin@admin.com", "admin").getToken().toString();
        } catch (CouponSystemException e) {
            throw new RuntimeException(e);
        }
    }


    public void addNewCompany(String token) {
        try {
            Company company1 = new Company(0, "company1", "company1@gmail.com", "1234", List.of());
            Company company2 = new Company(0, "company2", "company2@gmail.com", "1234", List.of());
            Company company3 = new Company(0, "company3", "company3@gmail.com", "1234", List.of());

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Company> requestEntity1 = new HttpEntity<>(company1, headers);
            HttpEntity<Company> requestEntity2 = new HttpEntity<>(company2, headers);
            HttpEntity<Company> requestEntity3 = new HttpEntity<>(company3, headers);

            ResponseEntity<Void> responseEntity1 = restTemplate.exchange(URL + "/company", HttpMethod.POST, requestEntity1, Void.class);
            ResponseEntity<Void> responseEntity2 = restTemplate.exchange(URL + "/company", HttpMethod.POST, requestEntity2, Void.class);
            ResponseEntity<Void> responseEntity3 = restTemplate.exchange(URL + "/company", HttpMethod.POST, requestEntity3, Void.class);

            System.out.println("Create company successfully " + responseEntity1.getStatusCode());
            System.out.println("Create company successfully " + responseEntity2.getStatusCode());
            System.out.println("Create company successfully " + responseEntity3.getStatusCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateCompany(String token) {
        try {
            Company company = new Company(1, "company1", "cola@gmail.com", "1234", List.of());
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Company> requestEntity1 = new HttpEntity<>(company, headers);
            ResponseEntity<Void> responseEntity = restTemplate.exchange(URL + "/company", HttpMethod.PUT, requestEntity1, Void.class);
            System.out.println("Update company successfully " + responseEntity.getStatusCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteCompany(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<Void> responseEntity = restTemplate.exchange(URL + "/company/{companyId}", HttpMethod.DELETE, requestEntity, Void.class, 1);
            System.out.println("Delete company successfully " + responseEntity.getStatusCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getAllCompanies(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<List<Company>> responseEntity = restTemplate.exchange(
                    URL + "/companies",
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<List<Company>>() {
                    }
            );

            List<Company> companies = responseEntity.getBody();
            System.out.println("All Companies successfully " + responseEntity.getStatusCode());
            System.out.println(companies);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getSingleCompany(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<Company> responseEntity = restTemplate.exchange(URL + "/company/2", HttpMethod.GET, requestEntity, Company.class
            );
            System.out.println("Get Single company successfully " + responseEntity.getBody());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addNewCustomer(String token) {
        try {
            Customer customer1 = new Customer(0, "customerName1", "customerLastName1", "customer1@gmail.com", "1234", List.of());
            Customer customer2 = new Customer(0, "customerName2", "customerLastName2", "customer2@gmail.com", "1234", List.of());
            Customer customer3 = new Customer(0, "customerName3", "customerLastName3", "customer3@gmail.com", "1234", List.of());

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Customer> requestEntity1 = new HttpEntity<>(customer1, headers);
            HttpEntity<Customer> requestEntity2 = new HttpEntity<>(customer2, headers);
            HttpEntity<Customer> requestEntity3 = new HttpEntity<>(customer3, headers);

            ResponseEntity<Void> responseEntity1 = restTemplate.exchange(URL + "/customer", HttpMethod.POST, requestEntity1, Void.class);
            ResponseEntity<Void> responseEntity2 = restTemplate.exchange(URL + "/customer", HttpMethod.POST, requestEntity2, Void.class);
            ResponseEntity<Void> responseEntity3 = restTemplate.exchange(URL + "/customer", HttpMethod.POST, requestEntity3, Void.class);

            System.out.println("Create customer successfully " + responseEntity1.getStatusCode());
            System.out.println("Create customer successfully " + responseEntity2.getStatusCode());
            System.out.println("Create customer successfully " + responseEntity3.getStatusCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateCustomer(String token) {
        try {
            Customer customer = new Customer(1, "Amit", "Zinchev", "amitZ@gmail.com", "1234", List.of());
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Customer> requestEntity1 = new HttpEntity<>(customer, headers);
            ResponseEntity<Void> responseEntity = restTemplate.exchange(URL + "/customer", HttpMethod.PUT, requestEntity1, Void.class);
            System.out.println("Update customer successfully " + responseEntity.getStatusCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteCustomer(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<Void> responseEntity = restTemplate.exchange(URL + "/customer/{customerId}", HttpMethod.DELETE, requestEntity, Void.class, 1);
            System.out.println("Delete customer successfully " + responseEntity.getStatusCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getAllCustomers(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<List<Customer>> responseEntity = restTemplate.exchange(
                    URL + "/customers",
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<List<Customer>>() {
                    }
            );

            List<Customer> customers = responseEntity.getBody();
            System.out.println("All Customers successfully " + responseEntity.getStatusCode());
            System.out.println(customers);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getSingleCustomer(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<Customer> responseEntity = restTemplate.exchange(URL + "/customer/2", HttpMethod.GET, requestEntity, Customer.class
            );
            System.out.println("Get Single company successfully " + responseEntity.getBody());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
