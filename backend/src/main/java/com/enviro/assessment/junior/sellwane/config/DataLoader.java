package com.enviro.assessment.junior.sellwane.config;

import com.enviro.assessment.junior.sellwane.model.Investor;
import com.enviro.assessment.junior.sellwane.model.Product;
import com.enviro.assessment.junior.sellwane.model.ProductType;
import com.enviro.assessment.junior.sellwane.model.SeedCredential;
import com.enviro.assessment.junior.sellwane.repository.InvestorRepository;
import com.enviro.assessment.junior.sellwane.repository.SeedCredentialRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    private static final String PASSWORD_CHARS =
            "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!@#$%";
    private static final int PASSWORD_LENGTH = 14;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final InvestorRepository investorRepository;
    private final SeedCredentialRepository seedCredentialRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(InvestorRepository investorRepository,
                      SeedCredentialRepository seedCredentialRepository,
                      PasswordEncoder passwordEncoder) {
        this.investorRepository = investorRepository;
        this.seedCredentialRepository = seedCredentialRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        // Guard: only seed once. Since H2 is file-based here, re-running on an
        // existing DB would otherwise create duplicate investors and mismatched passwords.
        if (investorRepository.count() > 0) {
            System.out.println("Seed data already present — skipping DataLoader.");
            return;
        }

        // Investor 1: over 65, so retirement withdrawals will be ALLOWED
        Investor sellwane = createInvestor("Sellwane Mosia", "sellwane.mosia@gmail.com",
                LocalDate.of(1955, 3, 12)); // age 71
        sellwane.getProducts().add(new Product(sellwane, "Retirement Annuity",
                ProductType.RETIREMENT_ANNUITY, new BigDecimal("500000.00")));
        sellwane.getProducts().add(new Product(sellwane, "Unit Trust - Balanced Fund",
                ProductType.UNIT_TRUST, new BigDecimal("120000.00")));

        // Investor 2: under 65, so retirement withdrawals will be REJECTED
        Investor matshepo = createInvestor("Matshepo Mthembu", "matshepo.mthembu@gmail.com",
                LocalDate.of(1990, 7, 21)); // age 36
        matshepo.getProducts().add(new Product(matshepo, "Living Annuity",
                ProductType.LIVING_ANNUITY, new BigDecimal("300000.00")));
        matshepo.getProducts().add(new Product(matshepo, "Tax-Free Savings Account",
                ProductType.TAX_FREE_SAVINGS, new BigDecimal("36000.00")));

        // Investor 3: under 65
        Investor sipho = createInvestor("Sipho Mokoena", "sipho.mokoena@gmail.com",
                LocalDate.of(1998, 11, 2)); // age 27
        sipho.getProducts().add(new Product(sipho, "Unit Trust - Equity Fund",
                ProductType.UNIT_TRUST, new BigDecimal("85000.00")));
        sipho.getProducts().add(new Product(sipho, "Living Annuity",
                ProductType.LIVING_ANNUITY, new BigDecimal("150000.00")));

        // Investor 4: over 65, so retirement withdrawals will be ALLOWED
        Investor annelize = createInvestor("Annelize van der Merwe", "annelize.vandermerwe@gmail.com",
                LocalDate.of(1958, 5, 30)); // age 68
        annelize.getProducts().add(new Product(annelize, "Retirement Annuity",
                ProductType.RETIREMENT_ANNUITY, new BigDecimal("720000.00")));
        annelize.getProducts().add(new Product(annelize, "Tax-Free Savings Account",
                ProductType.TAX_FREE_SAVINGS, new BigDecimal("40000.00")));

        // Investor 5: under 65
        Investor naledi = createInvestor("Naledi Khumalo", "naledi.khumalo@gmail.com",
                LocalDate.of(1985, 2, 14)); // age 41
        naledi.getProducts().add(new Product(naledi, "Unit Trust - Balanced Fund",
                ProductType.UNIT_TRUST, new BigDecimal("60000.00")));

        // Investor 6: exactly 65, so retirement withdrawals will still be REJECTED (must be OLDER than 65)
        Investor johan = createInvestor("Johan Pretorius", "johan.pretorius@gmail.com",
                LocalDate.of(1961, 9, 14)); // age 65
        johan.getProducts().add(new Product(johan, "Living Annuity",
                ProductType.LIVING_ANNUITY, new BigDecimal("410000.00")));
        johan.getProducts().add(new Product(johan, "Unit Trust - Equity Fund",
                ProductType.UNIT_TRUST, new BigDecimal("95000.00")));

        // Investor 7: over 65, so retirement withdrawals will be ALLOWED
        Investor grace = createInvestor("Grace Adeyemi", "grace.adeyemi@gmail.com",
                LocalDate.of(1950, 1, 8)); // age 76
        grace.getProducts().add(new Product(grace, "Retirement Annuity",
                ProductType.RETIREMENT_ANNUITY, new BigDecimal("610000.00")));
        grace.getProducts().add(new Product(grace, "Tax-Free Savings Account",
                ProductType.TAX_FREE_SAVINGS, new BigDecimal("30000.00")));

        investorRepository.save(sellwane);
        investorRepository.save(matshepo);
        investorRepository.save(sipho);
        investorRepository.save(annelize);
        investorRepository.save(naledi);
        investorRepository.save(johan);
        investorRepository.save(grace);

        System.out.println("Seed data loaded for " + investorRepository.count() +
                " investors. Login passwords are in the seed_credential table.");
    }

    private Investor createInvestor(String name, String email, LocalDate dob) {
        String rawPassword = generateSecurePassword();
        seedCredentialRepository.save(new SeedCredential(email, rawPassword));
        return new Investor(name, email, dob, passwordEncoder.encode(rawPassword));
    }

    private String generateSecurePassword() {
        StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            sb.append(PASSWORD_CHARS.charAt(RANDOM.nextInt(PASSWORD_CHARS.length())));
        }
        return sb.toString();
    }
}