package org.coupons.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Coupon {

    @Id
    @GeneratedValue
    Long id;

    String type; // CART, PRODUCT, BXGY
    @Column(columnDefinition = "json")
    String details; // store conditions as JSON
}
