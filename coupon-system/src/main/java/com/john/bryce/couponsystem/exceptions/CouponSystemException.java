package com.john.bryce.couponsystem.exceptions;

public class CouponSystemException extends Exception {
    public CouponSystemException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
