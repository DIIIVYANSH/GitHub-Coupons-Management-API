package org.coupons.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BxGyDetails {
    private List<ProductQuantity> buyProducts;
    private List<ProductQuantity> getProducts;
    private Integer repetitionLimit;
}
