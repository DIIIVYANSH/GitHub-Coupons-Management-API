package org.coupons.controller;

import org.coupons.entity.Coupon;
import org.coupons.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupons")
public class CouponController { //CRUD this will talk with repository

    private final CouponRepository couponRepository;

    @Autowired
    public CouponController(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @PostMapping
    public Coupon createCoupon(@RequestBody Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @GetMapping
    public List<Coupon> getAllCoupon() {
        return couponRepository.findAll();
    }

    @GetMapping("/{id}")
    public Coupon getCouponById(@PathVariable Long id) {
        return couponRepository.findById(id).orElseThrow( ()->new RuntimeException("Coupon with id " + id + " not found!") );
    }

    @PutMapping("/{id}")
    public Coupon updateCouponById(@PathVariable Long id,@RequestBody Coupon coupon) {
        Coupon existing = couponRepository.findById(id).orElseThrow( ()->new RuntimeException("Coupon with id " + id + " not found!") );

        existing.setDetails(coupon.getDetails());
        existing.setType(coupon.getType());
        return couponRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deleteCoupon(@PathVariable Long id) {
        couponRepository.deleteById(id);
    }


}
