package com.example.demo.controller;

import com.example.demo.model.Coupon;
import com.example.demo.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateCoupon(@RequestParam Integer parentId, @RequestParam String type) {
        if (!type.equalsIgnoreCase("monthly") && !type.equalsIgnoreCase("yearly")) {
            return ResponseEntity.badRequest().body("Invalid subscription type");
        }

        Coupon coupon = couponService.generateCoupon(parentId, type);
        return ResponseEntity.ok(coupon);
    }

    @PostMapping("/use")
    public ResponseEntity<?> useCoupon(@RequestParam Integer parentId, @RequestParam Integer couponId, @RequestParam String subscriptionType) {
        String result = couponService.useCoupon(parentId, couponId, subscriptionType);
        return ResponseEntity.ok(result);
    }
}