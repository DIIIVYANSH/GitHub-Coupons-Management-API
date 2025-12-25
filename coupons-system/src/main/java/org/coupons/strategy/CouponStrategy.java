package org.coupons.strategy;

import org.coupons.dto.CartDTO;
import org.coupons.entity.Coupon;

public interface CouponStrategy {
    boolean isApplicable(Coupon coupon, CartDTO cart);

    double calculateDiscount(Coupon coupon, CartDTO cart);

    CartDTO applyCoupon(Coupon coupon, CartDTO cart);
}
