package org.coupons.controller;

import org.coupons.dto.CartDTO;
import org.coupons.dtoresponse.ApplicableCouponResponse;
import org.coupons.dtoresponse.CartResponse;
import org.coupons.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CouponApplicationController {
    private final CouponService couponService;

    @Autowired
    public CouponApplicationController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/applicable-coupons")
    public List<ApplicableCouponResponse> getApplicableCoupons(@RequestBody CartDTO cartDTO) {
        return couponService.getApplicableCoupons(cartDTO);
    }

    @PostMapping("/apply-coupon/{id}")
    public CartResponse applyCoupon(
            @PathVariable Long id,
            @RequestBody CartDTO cart
    ) {
        return couponService.applyCoupon(id, cart);
    }



}
