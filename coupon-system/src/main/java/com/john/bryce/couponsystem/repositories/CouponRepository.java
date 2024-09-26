package com.john.bryce.couponsystem.repositories;

import com.john.bryce.couponsystem.entities.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    boolean existsByTitleAndCompanyId(String title, long id);

    boolean existsByIdAndCompanyId(long couponId, long companyId);

    @Query(value = "SELECT * FROM couponsystem2karina.coupon WHERE company_id = ?;", nativeQuery = true)
    List<Coupon> findByCompanyId(long companyId);

    @Query(value = "SELECT * FROM couponsystem2karina.coupon WHERE company_id = ? AND category = ?;", nativeQuery = true)
    List<Coupon> findByCompanyIdAndCategory(long companyId, String category);

    @Query(value = "SELECT * FROM couponsystem2karina.coupon WHERE company_id = ? AND price <= ?;", nativeQuery = true)
    List<Coupon> findAllByMaxPrice(long companyId, double price);

    @Modifying
    @Query(value = "INSERT INTO couponsystem2karina.customer_coupon (customer_id, coupon_id) VALUES (?1, ?2)", nativeQuery = true)
    void addCouponPurchase(long customerId, long couponId);

    @Query(value = "SELECT * FROM couponsystem2karina.coupon " +
            "INNER JOIN couponsystem2karina.customer_coupon " +
            "ON couponsystem2karina.customer_coupon.coupon_id=couponsystem2karina.coupon.id WHERE customer_id = ?;", nativeQuery = true)
    List<Coupon> findByCustomerId(long customerId);

    @Query(value = "SELECT * FROM couponsystem2karina.coupon " +
            "INNER JOIN couponsystem2karina.customer_coupon " +
            "ON couponsystem2karina.customer_coupon.coupon_id=couponsystem2karina.coupon.id WHERE customer_id = ? AND category = ?;", nativeQuery = true)
    List<Coupon> findByCustomerIdAndCategory(long customerId, String category);

    @Query(value = "SELECT * FROM couponsystem2karina.coupon " +
            "INNER JOIN couponsystem2karina.customer_coupon " +
            "ON couponsystem2karina.customer_coupon.coupon_id=couponsystem2karina.coupon.id WHERE customer_id = ? AND price <= ?;", nativeQuery = true)
    List<Coupon> getAllCouponsByCustomerIdAndMaxPrice(long customerId, double price);

    @Modifying
    @Query(value = "DELETE FROM couponsystem2karina.customer_coupon WHERE customer_id = ?;", nativeQuery = true)
    void deleteAllPurchasesCouponsByCustomerId(long customerId);

    @Modifying
    @Query(value = "DELETE FROM couponsystem2karina.customer_coupon WHERE coupon_id IN (select id from `coupon` where company_id = ?)", nativeQuery = true)
    void deletePurchasedCouponsByCompanyId(long companyId);


    @Query(value = "SELECT DISTINCT c.* FROM couponsystem2karina.coupon c " +
            "LEFT JOIN couponsystem2karina.customer_coupon cc " +
            "ON c.id = cc.coupon_id AND cc.customer_id = ?1 " +
            "WHERE cc.coupon_id IS NULL", nativeQuery = true)
    List<Coupon> findAllCouponsNotPurchasedByCustomer(long customerId);

}
