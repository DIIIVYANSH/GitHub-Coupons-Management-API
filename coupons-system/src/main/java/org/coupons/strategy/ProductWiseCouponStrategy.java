package org.coupons.strategy;

import org.coupons.dto.CartDTO;
import org.coupons.dto.CartItemDTO;
import org.coupons.dto.ProductWiseDetails;
import org.coupons.entity.Coupon;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component("PRODUCT")
public class ProductWiseCouponStrategy implements CouponStrategy {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean isApplicable(Coupon coupon, CartDTO cart) {
        ProductWiseDetails details = parseDetails(coupon.getDetails());

        return cart.getCartItemDTOList().stream()
                .anyMatch(item -> item.getProductId().equals(details.getProductId()));
    }

    @Override
    public double calculateDiscount(Coupon coupon, CartDTO cart) {
        if(!isApplicable(coupon, cart)) {
            return 0;
        }
        ProductWiseDetails details = parseDetails(coupon.getDetails());
        return cart.getCartItemDTOList()
                .stream().filter(item-> item.getProductId().equals(details.getProductId() ))
                .mapToDouble(item ->
                        (item.getPrice() * item.getQuantity() * details.getDiscount())/100)
                .sum();
    }

    @Override
    public CartDTO applyCoupon(Coupon coupon, CartDTO cart) {
        ProductWiseDetails details = parseDetails(coupon.getDetails());
        for(CartItemDTO item : cart.getCartItemDTOList()) {
            if(item.getProductId().equals(details.getProductId())) {
                double discount =
                        (item.getPrice() * item.getQuantity() * details.getDiscount())/100;
                item.setDiscount(discount);
            }
        }
        return cart;
    }

    private ProductWiseDetails parseDetails(String json) {
        try{
            return mapper.readValue(json, ProductWiseDetails.class);
        }
        catch(Exception e){
            throw new RuntimeException("Invalid product-wise coupon details" + e.getMessage());
        }
    }
}
