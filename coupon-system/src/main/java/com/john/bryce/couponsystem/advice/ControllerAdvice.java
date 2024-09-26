package com.john.bryce.couponsystem.advice;

import com.john.bryce.couponsystem.exceptions.CouponSystemException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = CouponSystemException.class)
    public ProblemDetail handleCatSystemException(CouponSystemException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ProblemDetail handleOtherException(Exception e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Something went wrong, please try again later");
    }
}
