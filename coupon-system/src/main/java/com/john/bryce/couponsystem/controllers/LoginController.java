package com.john.bryce.couponsystem.controllers;

import com.john.bryce.couponsystem.exceptions.CouponSystemException;
import com.john.bryce.couponsystem.security.LoginManager;
import com.john.bryce.couponsystem.security.LoginRequest;
import com.john.bryce.couponsystem.security.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
@CrossOrigin("*")
public class LoginController {
    private final LoginManager loginManager;

    @PostMapping
    public LoginResponse login(@RequestBody LoginRequest loginRequest) throws CouponSystemException {
        return loginManager.manageLogin(loginRequest.getEmail(), loginRequest.getPassword(), loginRequest.getClientType());
    }
}
