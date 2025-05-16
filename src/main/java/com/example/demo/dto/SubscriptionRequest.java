package com.example.demo.dto;

import lombok.Data;

@Data
public class SubscriptionRequest {
    private String type; // "monthly" , "yearly"
    private String paymentMethod; // "direct" , "coupon"
    private Integer couponId; // optional, only needed if paymentMethod = coupon
}
