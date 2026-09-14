package com.enviro.assessment.junior.sellwane.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// Dev/demo only: stores plaintext seed passwords for local testing and review purposes.
// This must never be used or replicated in a production data model.
@Entity
public class SeedCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String plaintextPassword;

    protected SeedCredential() {
        // JPA
    }

    public SeedCredential(String email, String plaintextPassword) {
        this.email = email;
        this.plaintextPassword = plaintextPassword;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPlaintextPassword() {
        return plaintextPassword;
    }
}