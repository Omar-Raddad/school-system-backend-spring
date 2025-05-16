package com.example.demo.controller;

import com.example.demo.model.Coupon;
import com.example.demo.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.CreateCouponRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/use")
    public ResponseEntity<?> useCoupon(@RequestParam Integer parentId, @RequestParam Integer couponId, @RequestParam String subscriptionType) {
//        String result = couponService.useCoupon(parentId, couponId, subscriptionType);
        Coupon result = couponService.useCoupon(parentId, couponId, subscriptionType);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/admin/create")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public ResponseEntity<?> createCoupon(@RequestBody CreateCouponRequest request) {
        try {
            Coupon newCoupon = couponService.createCouponByAdmin(request);
            return ResponseEntity.ok(newCoupon);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

}