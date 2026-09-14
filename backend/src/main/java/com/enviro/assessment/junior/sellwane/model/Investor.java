package com.enviro.assessment.junior.sellwane.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an investor at Enviro365. This is a JPA "entity", meaning
 * Hibernate will automatically create a table called INVESTOR for it
 * in the H2 database, based on these fields.
 */
@Entity
@Table(name = "investor")
public class Investor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment primary key
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private String password;

    /**
     * One investor can have many products (unit trusts, retirement annuities, etc.)
     * mappedBy = "investor" tells JPA that the Product entity owns the foreign key
     * (see the @ManyToOne on Product.investor).
     * cascade = ALL means: if we save/delete an Investor, its Products are saved/deleted too.
     */
    @OneToMany(mappedBy = "investor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    // JPA requires a no-argument constructor
    public Investor() {
    }

    public Investor(String fullName, String email, LocalDate dateOfBirth) {
        this.fullName = fullName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
    }

    public Investor(String fullName, String email, LocalDate dateOfBirth, String password) {
        this(fullName, email, dateOfBirth);
        this.password = password;
    }

    /**
     * Calculated (not stored) property: works out the investor's current
     * age from their date of birth. Used by the "age > 65" business rule.
     */
    @Transient
    public int getAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    // ----- Getters and setters -----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
