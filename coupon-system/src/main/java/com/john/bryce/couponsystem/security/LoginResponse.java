package com.john.bryce.couponsystem.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private long id;
    private UUID token;
    private String email;
    private String name;
    private ClientType clientType;

}
