# Coupons Management API

A backend service for evaluating and applying discount coupons in an e-commerce cart.  
The system supports **Product-wise**, **Cart-wise**, and **Buy X Get Y (BXGY)** coupons and is designed using the **Strategy Pattern** for clean extensibility.

---

## Key Features

- Supports multiple coupon types with independent business rules
- Preview applicable coupons without mutating the cart
- Apply a selected coupon and return item-level discounts
- Prevents over-discounting and handles edge cases safely
- Easily extensible architecture for new coupon types

---

## Tech Stack

- Java 17  
- Spring Boot  
- Spring Data JPA  
- MySQL  
- Jackson (JSON parsing)  
- Postman (manual API testing)

---

## High-Level Design

The application uses the **Strategy Pattern** to isolate coupon logic:

```
Controller → Service → Strategy Factory → Coupon Strategy → Repository → MySQL
```

Each coupon type is implemented as a separate strategy.  
Adding a new coupon type requires **no changes** to existing strategies.

---

## Implemented Coupon Types

### 1. Product-wise Coupon
- Applies a percentage discount to a specific product
- Coupon is applicable only if the product exists in the cart

### 2. Cart-wise Coupon
- Applies a percentage discount on the entire cart
- Activated only when cart total crosses a defined threshold
- Discount is distributed proportionally across cart items

### 3. Buy X Get Y (BXGY) Coupon
- Buy a certain quantity of one product
- Get another product free
- Supports repetition limits
- Cheapest eligible items are selected as free items

---

## BXGY Logic Summary

1. Calculate total buy-product quantity in the cart  
2. Determine how many times the coupon can repeat  
3. Apply repetition limits  
4. Identify eligible free products  
5. Apply discount equal to the price of free items  

This ensures **financial correctness** and prevents misuse.

---

## API Endpoints

### Coupon Management
- `POST /coupons`
- `GET /coupons`
- `GET /coupons/{id}`
- `PUT /coupons/{id}`
- `DELETE /coupons/{id}`

### Business APIs
- `POST /applicable-coupons`  
  Returns all applicable coupons with calculated discounts (preview only)

- `POST /apply-coupon/{id}`  
  Applies a single coupon and returns the updated cart

---

## Testing

- All APIs were manually tested using **Postman**
- Verified scenarios:
  - Valid and invalid coupons
  - Multiple applicable coupons
  - BXGY repetition limits
  - Cart and product edge cases

---

## Assumptions

- Only one coupon can be applied at a time
- Cart data provided in request is trusted
- Prices are static during evaluation
- Inventory validation is out of scope
- Taxes and shipping are not considered

---

## Limitations

- Coupon stacking is not supported
- No coupon expiry validation
- No user-specific usage limits
- No category or brand-level coupons
- Limited automated test coverage

---

## Future Improvements

- Coupon expiry and scheduling
- Coupon stacking with priority rules
- Category and brand-based coupons
- Rule-engine based coupon evaluation
- Unit and integration tests
- User-level coupon usage tracking

---

## Conclusion

This project focuses on **clean design**, **correct business logic**, and **extensibility**.  
The architecture avoids tightly coupled logic and ensures that new coupon types can be added with minimal effort.

---

**Author:** Divyansh Sapkale  
**Role:** Backend Developer (Java / Spring Boot)
