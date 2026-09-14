package com.enviro.assessment.junior.sellwane.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * This is the JSON body the frontend sends to POST /api/withdrawals, e.g.:
 * {
 *   "productId": 1,
 *   "amount": 5000.00
 * }
 *
 * This class only exists to describe what a withdrawal REQUEST looks like -
 * it is not a full "DTO layer" over every entity, just the one small class
 * that lets us attach validation rules to incoming data.
 *
 * The annotations below are our "Input validation" advanced requirement.
 * Spring checks these automatically when the controller method parameter
 * is annotated with @Valid - if a rule is broken, Spring throws a
 * MethodArgumentNotValidException BEFORE our code even runs, and our
 * GlobalExceptionHandler turns that into a clean 400 response.
 */
public class WithdrawalRequest {

    @NotNull(message = "productId is required")
    private Long productId;

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.01", message = "amount must be greater than 0")
    private BigDecimal amount;

    public WithdrawalRequest() {
    }

    public WithdrawalRequest(Long productId, BigDecimal amount) {
        this.productId = productId;
        this.amount = amount;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
