package com.example.demo.dto;

import lombok.Data;

@Data
public class CreateCouponRequest {
    private String type; // "monthly" , "yearly"
    private double discountValue;
    private boolean singleChildOnly;
    private int usageLimit;
    private int validDays; // days from now
}
