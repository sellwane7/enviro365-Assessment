package com.enviro.assessment.junior.sellwane.model;

/**
 * The different types of investment products an investor can hold.
 *
 * This matters for our business rules: RETIREMENT_ANNUITY and LIVING_ANNUITY
 * are "retirement" products, and the assessment rule says:
 *   "Retirement withdrawals only allowed if age > 65"
 * So we need a way to know, from the product itself, whether it is subject
 * to that rule.
 */
public enum ProductType {
    RETIREMENT_ANNUITY(true),
    LIVING_ANNUITY(true),
    UNIT_TRUST(false),
    TAX_FREE_SAVINGS(false);

    private final boolean retirementProduct;

    ProductType(boolean retirementProduct) {
        this.retirementProduct = retirementProduct;
    }

    public boolean isRetirementProduct() {
        return retirementProduct;
    }
}
