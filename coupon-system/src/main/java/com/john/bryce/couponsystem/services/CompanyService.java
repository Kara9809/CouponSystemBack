package com.john.bryce.couponsystem.services;

import com.john.bryce.couponsystem.entities.Category;
import com.john.bryce.couponsystem.entities.Company;
import com.john.bryce.couponsystem.entities.Coupon;
import com.john.bryce.couponsystem.exceptions.CouponSystemException;
import com.john.bryce.couponsystem.exceptions.ErrorMessage;
import com.john.bryce.couponsystem.repositories.CompanyRepository;
import com.john.bryce.couponsystem.repositories.CouponRepository;
import com.john.bryce.couponsystem.security.ClientType;
import com.john.bryce.couponsystem.security.Information;
import com.john.bryce.couponsystem.security.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService extends ClientService {

    public LoginResponse login(String email, String password) throws CouponSystemException {
        Company companyFromDb = companyRepository.findByEmailAndPassword(email, password).orElseThrow(() -> new CouponSystemException(ErrorMessage.COMPANY_LOGIN_ERROR));

        Information information = new Information(companyFromDb.getId(), email, ClientType.COMPANY, LocalDateTime.now().plusDays(1));
        UUID token = tokenManager.addToken(information);

        return new LoginResponse(companyFromDb.getId(), token, email, companyFromDb.getName(), ClientType.COMPANY);
    }

    @Transactional
    public Coupon addCoupon(UUID token, Coupon coupon) throws CouponSystemException {
        long companyId = tokenManager.validateToken(token, ClientType.COMPANY);

        if (!companyRepository.existsById(companyId)) {
            throw new CouponSystemException(ErrorMessage.NOT_EXIST_COMPANY);
        }
        if (couponRepository.existsByTitleAndCompanyId(coupon.getTitle(), companyId)) {
            throw new CouponSystemException(ErrorMessage.COUPON_TITLE_EXIST);
        }
        if (!coupon.getImage().contains("https://")) {
            coupon.setImage("https://picsum.photos/200/300?hmac=" + Math.random() * 9_999);
        }

        Company companyFromDb = companyRepository.findById(companyId).orElseThrow(() -> new CouponSystemException(ErrorMessage.NOT_EXIST_COMPANY));
        coupon.setCompany(companyFromDb);
        couponRepository.save(coupon);

        return coupon;
    }


    @Transactional
    public void updateCoupon(UUID token, Coupon coupon) throws CouponSystemException {
        long companyId = tokenManager.validateToken(token, ClientType.COMPANY);

        Coupon couponFromDb = couponRepository.findById(coupon.getId()).orElseThrow(() -> new CouponSystemException(ErrorMessage.COUPON_UPDATE_ID));
        // i choose couponFromDb ID and not coupon - just to check!
        if (!couponRepository.existsByIdAndCompanyId(couponFromDb.getId(), companyId)) {
            throw new CouponSystemException(ErrorMessage.COUPON_UPDATE_COMP_ID);
        }

        Company companyFromDb = companyRepository.findById(companyId).orElseThrow(() -> new CouponSystemException(ErrorMessage.NOT_EXIST_COMPANY));

        coupon.setCompany(companyFromDb);

        couponFromDb.setCompany(coupon.getCompany());
        couponFromDb.setCategory(coupon.getCategory());
        couponFromDb.setTitle(coupon.getTitle());
        couponFromDb.setDescription(coupon.getDescription());
        couponFromDb.setStartDate(coupon.getStartDate());
        couponFromDb.setEndDate(coupon.getEndDate());
        couponFromDb.setAmount(coupon.getAmount());
        couponFromDb.setPrice(coupon.getPrice());
        couponFromDb.setImage(coupon.getImage());

        couponRepository.save(couponFromDb);
    }

    @Transactional
    public void deleteCoupon(UUID token, long couponId) throws CouponSystemException {
        long companyId = tokenManager.validateToken(token, ClientType.COMPANY);

        if (!couponRepository.existsByIdAndCompanyId(couponId, companyId)) {
            throw new CouponSystemException(ErrorMessage.COUPON_OR_COMPANY_NOT_EXIST);
        }
        couponRepository.deleteById(couponId);
    }

    public List<Coupon> getCompanyCoupons(UUID token) throws CouponSystemException {
        long companyId = tokenManager.validateToken(token, ClientType.COMPANY);

        Company companyFromDb = companyRepository.findById(companyId).orElseThrow(() -> new CouponSystemException(ErrorMessage.NOT_EXIST_COMPANY));

        return couponRepository.findByCompanyId(companyFromDb.getId());
    }

    public List<Coupon> getCompanyCouponsByCategory(UUID token, Category category) throws CouponSystemException {
        long companyId = tokenManager.validateToken(token, ClientType.COMPANY);

        Company companyFromDb = companyRepository.findById(companyId).orElseThrow(() -> new CouponSystemException(ErrorMessage.NOT_EXIST_COMPANY));

        return couponRepository.findByCompanyIdAndCategory(companyFromDb.getId(), category.name());
    }

    public List<Coupon> getCompanyCouponsByMaxPrice(UUID token, double maxPrice) throws CouponSystemException {
        long companyId = tokenManager.validateToken(token, ClientType.COMPANY);

        Company companyFromDb = companyRepository.findById(companyId).orElseThrow(() -> new CouponSystemException(ErrorMessage.NOT_EXIST_COMPANY));

        return couponRepository.findAllByMaxPrice(companyFromDb.getId(), maxPrice);
    }

    public Company getCompanyDetails(UUID token) throws CouponSystemException {
        long companyId = tokenManager.validateToken(token, ClientType.COMPANY);

        return companyRepository.findById(companyId).orElseThrow(() -> new CouponSystemException(ErrorMessage.NOT_EXIST_COMPANY));
    }
}
