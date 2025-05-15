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


@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final ParentService parentService;
    private final CouponUsageRepository couponUsageRepository;

    public Coupon generateCoupon(Integer parentId, String type) {
        int childrenCount = parentService.countChildren(parentId);
        System.out.println("Parent ID " + parentId + " has " + childrenCount + " child(ren)");

        Coupon coupon = Coupon.builder()
                .code("SAVE" + new Random().nextInt(10000))
                .discountType(type)
                .discountValue(new Random().nextBoolean() ? 10 : 15)
                .usageLimit(2)
                .singleChildOnly(childrenCount == 1)
                .createdAt(LocalDateTime.now())
                .validDays(2)
                .build();

        return couponRepository.save(coupon);
    }

    public String useCoupon(Integer parentId, Integer couponId, String subscriptionType) {
        Optional<Coupon> couponOpt = couponRepository.findById(couponId);
        long usageCount = couponUsageRepository.countByUserIdAndCouponId(parentId, couponId);

        if (couponOpt.isEmpty()) {
            throw new RuntimeException("Coupon not found");
        }

        Coupon coupon = couponOpt.get();

        if (!coupon.getDiscountType().equalsIgnoreCase(subscriptionType)) {
            throw new RuntimeException("Coupon type does not match subscription type");
        }

        if (coupon.getSingleChildOnly() && parentService.countChildren(parentId) != 1) {
            throw new RuntimeException("This coupon is only valid for parents with a single child");
        }

        if (couponUsageRepository.findAll().stream().filter(u -> u.getCouponId().equals(couponId)).count() >= coupon.getUsageLimit()) {
            throw new RuntimeException("Coupon usage limit reached");
        }

//        boolean alreadyUsed = couponUsageRepository.existsByUserIdAndCouponId(parentId, couponId);
//        if (alreadyUsed) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Coupon already used by this parent");
//        }

        if (usageCount >= 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Coupon usage limit exceeded");
        }

        CouponUsage usage = CouponUsage.builder()
                .couponId(couponId)
                .userId(parentId)
                .usedAt(LocalDateTime.now())
                .build();

        couponUsageRepository.save(usage);
        return "Coupon applied successfully";
    }
}
