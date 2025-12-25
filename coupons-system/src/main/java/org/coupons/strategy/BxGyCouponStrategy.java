package org.coupons.strategy;

import org.coupons.dto.BxGyDetails;
import org.coupons.dto.CartDTO;
import org.coupons.dto.CartItemDTO;
import org.coupons.dto.ProductQuantity;
import org.coupons.entity.Coupon;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.Comparator;
import java.util.List;

@Component("BXGY")
public class BxGyCouponStrategy implements CouponStrategy {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean isApplicable(Coupon coupon, CartDTO cart) {
        BxGyDetails details = parseDetails(coupon.getDetails());
        if (details.getBuyProducts() == null || details.getBuyProducts().isEmpty()) {
            return false;
        }

        int totalBuyQty = cart.getCartItemDTOList()
                .stream().filter(item-> isBuyProduct(item,details))
                .mapToInt(CartItemDTO::getQuantity).sum();
        int reqBuyQty = details.getBuyProducts()
                .stream().mapToInt(ProductQuantity::getQuantity).sum();

        System.out.println("BUY PRODUCTS = " + details.getBuyProducts());
        System.out.println("GET PRODUCTS = " + details.getGetProducts());
        System.out.println("REPETITION = " + details.getRepetitionLimit());


        return totalBuyQty >= reqBuyQty;
    }

    @Override
    public double calculateDiscount(Coupon coupon, CartDTO cart) {
        if (!isApplicable(coupon, cart)) return 0;

        BxGyDetails details = parseDetails(coupon.getDetails());

        int totalBuyQty = cart.getCartItemDTOList().stream()
                .filter(item -> isBuyProduct(item, details))
                .mapToInt(CartItemDTO::getQuantity)
                .sum();

        int requiredBuyQty = details.getBuyProducts()
                .stream()
                .mapToInt(ProductQuantity::getQuantity)
                .sum();

        int repetitions = Math.min(
                totalBuyQty / requiredBuyQty,
                details.getRepetitionLimit()
        );

        List<CartItemDTO> freeCandidates = cart.getCartItemDTOList().stream()
                .filter(item -> isGetProduct(item, details))
                .sorted(Comparator.comparingDouble(CartItemDTO::getPrice))
                .toList();

        int freeItemsAllowed = repetitions *
                details.getGetProducts().stream().mapToInt(ProductQuantity::getQuantity).sum();

        double discount = 0;
        int count = 0;

        for (CartItemDTO item : freeCandidates) {
            for (int i = 0; i < item.getQuantity() && count < freeItemsAllowed; i++) {
                discount += item.getPrice();
                count++;
            }
            if (count >= freeItemsAllowed) break;
        }

        return discount;
    }


    @Override
    public CartDTO applyCoupon(Coupon coupon, CartDTO cart) {
        double discount = calculateDiscount(coupon, cart);
        if (discount == 0) return cart;

        BxGyDetails details = parseDetails(coupon.getDetails());

        int freeItemsAllowed = calculateFreeItemCount(details, cart);

        List<CartItemDTO> freeCandidates = cart.getCartItemDTOList().stream()
                .filter(item -> isGetProduct(item, details))
                .sorted(Comparator.comparingDouble(CartItemDTO::getPrice))
                .toList();

        int count = 0;

        for (CartItemDTO item : freeCandidates) {
            int freeQty = Math.min(item.getQuantity(), freeItemsAllowed - count);
            if (freeQty > 0) {
                item.setDiscount(item.getPrice() * freeQty);
                count += freeQty;
            }
            if (count >= freeItemsAllowed) break;
        }

        return cart;
    }

    private BxGyDetails parseDetails(String json) {
        try {
            return objectMapper.readValue(json, BxGyDetails.class);
        }
        catch (Exception e) {
            throw new RuntimeException("Invalid BXGY coupon details");
        }
    }

    private boolean isBuyProduct(CartItemDTO item, BxGyDetails details) {
        return details.getBuyProducts().stream()
                .anyMatch(p -> p.getProductId().equals(item.getProductId()));
    }

    private boolean isGetProduct(CartItemDTO item, BxGyDetails details) {
        return details.getGetProducts().stream()
                .anyMatch(p -> p.getProductId().equals( item.getProductId()) );
    }

    private int calculateFreeItemCount(BxGyDetails details, CartDTO cart) {
        int totalBuyQty = cart.getCartItemDTOList().stream()
                .filter(item -> isBuyProduct(item, details))
                .mapToInt(CartItemDTO::getQuantity)
                .sum();

        int requiredBuyQty = details.getBuyProducts()
                .stream()
                .mapToInt(ProductQuantity::getQuantity)
                .sum();

        int repetitions = Math.min(
                totalBuyQty / requiredBuyQty,
                details.getRepetitionLimit()
        );

        return repetitions *
                details.getGetProducts().stream().mapToInt(ProductQuantity::getQuantity).sum();
    }
}
