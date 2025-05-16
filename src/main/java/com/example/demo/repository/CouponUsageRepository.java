package com.example.demo.repository;

import com.example.demo.model.CouponUsage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponUsageRepository extends JpaRepository<CouponUsage, Integer> {
    boolean existsByUserIdAndCouponId(Integer userId, Integer couponId);
    long countByCouponId(Integer couponId);

}



