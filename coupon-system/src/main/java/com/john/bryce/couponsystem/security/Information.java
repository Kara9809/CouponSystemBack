package com.john.bryce.couponsystem.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Information {
    private long id;
    private String email;
    private ClientType clientType;
    private LocalDateTime expiration;
}
