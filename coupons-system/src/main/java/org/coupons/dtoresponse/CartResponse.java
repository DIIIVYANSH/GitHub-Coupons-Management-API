package org.coupons.dtoresponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.coupons.dto.CartDTO;

@Data
@AllArgsConstructor
public class CartResponse {

    private CartDTO cart;
    private double totalDiscount;
    private double finalPrice;
}
