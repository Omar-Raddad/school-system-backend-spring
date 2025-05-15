package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;

    @Column(name = "discount_type")
    private String discountType; //"monthly", "yearly"

    @Column(name = "discount_value")
    private Integer discountValue;

    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Column(name = "single_child_only")
    private Boolean singleChildOnly;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "valid_days")
    private Integer validDays;
}