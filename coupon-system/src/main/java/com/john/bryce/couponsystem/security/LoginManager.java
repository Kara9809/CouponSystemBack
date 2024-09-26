package com.john.bryce.couponsystem.security;

import com.john.bryce.couponsystem.exceptions.CouponSystemException;
import com.john.bryce.couponsystem.exceptions.ErrorMessage;
import com.john.bryce.couponsystem.repositories.CustomerRepository;
import com.john.bryce.couponsystem.services.AdminService;
import com.john.bryce.couponsystem.services.CompanyService;
import com.john.bryce.couponsystem.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginManager {
    private final AdminService adminService;
    private final CompanyService companyService;
    private final CustomerService customerService;

    public LoginResponse manageLogin(String email, String password, ClientType clientType) throws CouponSystemException {
        LoginResponse loginResponse = null;
        switch (clientType) {
            case ADMIN: {
                loginResponse = adminService.login(email, password);
                break;
            }
            case COMPANY: {
                loginResponse = companyService.login(email, password);
                break;
            }
            case CUSTOMER: {
                loginResponse = customerService.login(email, password);
                break;
            }
            default: {
                throw new CouponSystemException(ErrorMessage.INVALID_CLIENT_TYPE);
            }
        }
        return loginResponse;
    }
}
