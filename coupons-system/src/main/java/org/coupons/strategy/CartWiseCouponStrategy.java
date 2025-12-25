package org.coupons.strategy;

import org.coupons.dto.CartDTO;
import org.coupons.dto.CartItemDTO;
import org.coupons.dto.CartWiseDetails;
import org.coupons.entity.Coupon;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component("CART")
public class CartWiseCouponStrategy implements CouponStrategy {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean isApplicable(Coupon coupon, CartDTO cart) {
        double cartTotal = cart.getCartItemDTOList()
                .stream().mapToDouble(item-> item.getPrice()*item.getQuantity())
                .sum();
        CartWiseDetails details=parseDetails(coupon.getDetails());
        return cartTotal >= details.getThreshold();
    }

    @Override
    public double calculateDiscount(Coupon coupon, CartDTO cart) {
        if(!isApplicable(coupon, cart)) {
            return 0;
        }
        double cartTotal = cart.getCartItemDTOList()
                .stream().mapToDouble(item-> item.getPrice()*item.getQuantity())
                .sum();
        CartWiseDetails details = parseDetails(coupon.getDetails());
        return (cartTotal* details.getDiscount())/100;
    }

    @Override
    public CartDTO applyCoupon(Coupon coupon, CartDTO cart) {
        double discount = calculateDiscount(coupon, cart);
        if(discount == 0) return cart;

        double cartTotal = cart.getCartItemDTOList()
                .stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        for( CartItemDTO item : cart.getCartItemDTOList() ) {
            double itemTotal = item.getPrice()*item.getQuantity();
            double itemDiscount = ( itemTotal/cartTotal ) * discount;
            item.setDiscount(itemDiscount);
        }
        return cart;
    }

    private CartWiseDetails parseDetails(String json) {
        try{
            return mapper.readValue(json, CartWiseDetails.class);
        }
        catch (Exception e){
            throw new RuntimeException("Invalid CartWiseDetails Coupon details");
        }
    }
}
