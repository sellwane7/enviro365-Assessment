package com.enviro.assessment.junior.sellwane.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * Represents a single investment product/holding, e.g. "Retirement Annuity"
 * or "Unit Trust", that belongs to one investor and has a monetary balance.
 *
 * We use BigDecimal (not double/float) for money, because floating point
 * numbers lose precision and can cause cent-level rounding bugs - never use
 * double for currency in real financial systems.
 */
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Many products can belong to one investor.
     * @JoinColumn creates an "investor_id" foreign key column in the PRODUCT table.
     */
    /**
     * @JsonIgnore is important here: without it, converting a Product to JSON
     * would include its Investor, which includes its list of Products, which
     * includes their Investor again... an infinite loop that would crash the
     * app. We simply don't send the investor back down inside a product -
     * the frontend already knows which investor it asked for.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investor_id", nullable = false)
    @JsonIgnore
    private Investor investor;

    @Column(nullable = false)
    private String productName;

    @Enumerated(EnumType.STRING) // store the enum as text (e.g. "UNIT_TRUST") not a number
    @Column(nullable = false)
    private ProductType productType;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    public Product() {
    }

    public Product(Investor investor, String productName, ProductType productType, BigDecimal balance) {
        this.investor = investor;
        this.productName = productName;
        this.productType = productType;
        this.balance = balance;
    }

    // ----- Getters and setters -----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Investor getInvestor() {
        return investor;
    }

    public void setInvestor(Investor investor) {
        this.investor = investor;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
