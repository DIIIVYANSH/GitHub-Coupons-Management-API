package org.coupons.strategy;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CouponStrategyFactory {

    private final Map<String, CouponStrategy> couponStrategyMap;

    public CouponStrategyFactory(Map<String, CouponStrategy> couponStrategyMap) {
        this.couponStrategyMap = couponStrategyMap;
    }
    public CouponStrategy getStrategy(String type) {
        CouponStrategy couponStrategy = couponStrategyMap.get(type);
        if (couponStrategy == null) {
            throw new RuntimeException("Unsupported Coupon type " + type);
        }
        return couponStrategy;
    }
}

