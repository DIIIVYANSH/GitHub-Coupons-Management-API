package org.coupons.service;

import org.coupons.dto.CartDTO;
import org.coupons.dto.CartItemDTO;
import org.coupons.dtoresponse.ApplicableCouponResponse;
import org.coupons.dtoresponse.CartResponse;
import org.coupons.entity.Coupon;
import org.coupons.repository.CouponRepository;
import org.coupons.strategy.CouponStrategy;
import org.coupons.strategy.CouponStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponStrategyFactory strategyFactory;

    @Autowired
    public CouponService(
            CouponRepository couponRepository,
            CouponStrategyFactory strategyFactory
    ) {
        this.couponRepository = couponRepository;
        this.strategyFactory = strategyFactory;
    }

    public List<ApplicableCouponResponse> getApplicableCoupons(CartDTO cart) {

        List<Coupon> coupons = couponRepository.findAll();
        List<ApplicableCouponResponse> response = new ArrayList<>();

        for (Coupon coupon : coupons) {
            CouponStrategy strategy =
                    strategyFactory.getStrategy(coupon.getType());

            if (strategy.isApplicable(coupon, cart)) {
                double discount =
                        strategy.calculateDiscount(coupon, cart);

                response.add(
                        new ApplicableCouponResponse(
                                coupon.getId(),
                                coupon.getType(),
                                discount
                        )
                );
            }
        }
        return response;
    }

    public CartResponse applyCoupon(Long couponId, CartDTO cart) {

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));

        CouponStrategy strategy =
                strategyFactory.getStrategy(coupon.getType());

        if (!strategy.isApplicable(coupon, cart)) {
            throw new RuntimeException("Coupon not applicable");
        }

        CartDTO updatedCart =
                strategy.applyCoupon(coupon, cart);

        double totalDiscount = updatedCart.getCartItemDTOList()
                .stream()
                .mapToDouble(CartItemDTO::getDiscount)
                .sum();

        double totalPrice = updatedCart.getCartItemDTOList()
                .stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        return new CartResponse(
                updatedCart,
                totalDiscount,
                totalPrice - totalDiscount
        );
    }


}
