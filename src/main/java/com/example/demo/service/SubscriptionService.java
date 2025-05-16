package com.example.demo.service;

import com.example.demo.dto.SubscriptionRequest;
import com.example.demo.model.*;
import com.example.demo.repository.ChildRepository;
import com.example.demo.repository.CouponUsageRepository;
import com.example.demo.repository.ParentRepository;
import com.example.demo.repository.SubscriptionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final CouponService couponService;
    private final CouponUsageRepository couponUsageRepository;

    @Transactional
    public void subscribeParent(SubscriptionRequest request, String parentEmail) {
        Parent parent = parentRepository.findByEmail(parentEmail)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        if (subscriptionRepository.existsByUserIdAndIsActiveTrue(parent.getId())) {
            throw new IllegalArgumentException("You already have an active subscription.");
        }

        List<Child> children = childRepository.findByParentId(parent.getId());
        if (children.isEmpty()) {
            throw new IllegalArgumentException("No children found for subscription.");
        }

        int basePrice = request.getType().equalsIgnoreCase("monthly") ? 100 : 1000;
        boolean usingCoupon = request.getPaymentMethod().equalsIgnoreCase("coupon");

        Coupon coupon = null;
        if (usingCoupon) {
            coupon = couponService.validateCoupon(request.getCouponId(), request.getType());
        }

        for (int i = 0; i < children.size(); i++) {
            Child child = children.get(i);
            int price = basePrice;

            if (usingCoupon) {
                boolean applyToThisChild = !coupon.getSingleChildOnly() || i == 0;
                if (applyToThisChild) {
                    price -= (coupon.getDiscountValue() * basePrice / 100);
                    couponUsageRepository.save(CouponUsage.builder()
                            .couponId(coupon.getId())
                            .userId(Math.toIntExact(parent.getId()))
                            .usedAt(LocalDateTime.now())
                            .build());
                }
            } else {
                if (i > 0) {
                    price = (int) (basePrice * 0.8); // 20% off
                }
            }

            Subscription subscription = Subscription.builder()
                    .userId(Math.toIntExact(parent.getId()))
                    .childId(child.getId())
                    .type(request.getType())
                    .paymentMethod(request.getPaymentMethod())
                    .couponId(usingCoupon ? coupon.getId() : null)
                    .isActive(true)
                    .startDate(LocalDate.now())
                    .endDate(request.getType().equalsIgnoreCase("monthly") ?
                            LocalDate.now().plusMonths(1) : LocalDate.now().plusYears(1))
                    .build();

            subscriptionRepository.save(subscription);
            System.out.println("Child " + child.getId() + " charged: $" + price);
        }
    }

}
