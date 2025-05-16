package com.example.demo.service;

import com.example.demo.model.Coupon;
import com.example.demo.model.CouponUsage;
import com.example.demo.repository.CouponRepository;
import com.example.demo.repository.CouponUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.time.LocalDate;
import java.util.UUID;
import com.example.demo.dto.CreateCouponRequest;



@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final ParentService parentService;
    private final CouponUsageRepository couponUsageRepository;

    public Coupon createCouponByAdmin(CreateCouponRequest request) {
        Coupon coupon = new Coupon();

        coupon.setCode(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        coupon.setDiscountType(request.getType());
        coupon.setDiscountValue((int) request.getDiscountValue());
        coupon.setUsageLimit(request.getUsageLimit());
        coupon.setSingleChildOnly(request.isSingleChildOnly());
        coupon.setCreatedAt(LocalDate.now().atStartOfDay());
        coupon.setValidDays(request.getValidDays());

        return couponRepository.save(coupon);
    }

    public Coupon useCoupon(Integer parentId, Integer couponId, String subscriptionType) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));

        if (!coupon.getDiscountType().equalsIgnoreCase(subscriptionType)) {
            throw new RuntimeException("Coupon type does not match subscription type");
        }

        if (!coupon.getCreatedAt().plusDays(coupon.getValidDays()).isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Coupon expired");
        }

        boolean alreadyUsed = couponUsageRepository.existsByUserIdAndCouponId(parentId, couponId);
        if (alreadyUsed) {
            throw new RuntimeException("Coupon already used by this parent.");
        }

        couponUsageRepository.save(CouponUsage.builder()
                .couponId(couponId)
                .userId(parentId)
                .usedAt(LocalDateTime.now())
                .build());

        return coupon;
    }

    public Coupon validateCoupon(Integer couponId, String expectedType) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid coupon ID."));

        if (!coupon.getDiscountType().equalsIgnoreCase(expectedType)) {
            throw new IllegalArgumentException("Coupon type mismatch.");
        }

        long usageCount = couponUsageRepository.countByCouponId(couponId);
        if (usageCount >= coupon.getUsageLimit()) {
            throw new IllegalArgumentException("Coupon usage limit reached.");
        }

        LocalDateTime expiryDate = coupon.getCreatedAt().plusDays(coupon.getValidDays());
        if (expiryDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Coupon expired.");
        }

        return coupon;
    }

}
