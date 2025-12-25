package org.coupons.dtoresponse;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicableCouponResponse {

    private Long couponId;
    private String type;
    private double discount;

}
