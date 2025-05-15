package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "coupon_usages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "coupon_id")
    private Integer couponId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "used_at")
    private LocalDateTime usedAt;
}
