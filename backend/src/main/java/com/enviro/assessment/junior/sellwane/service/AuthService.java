package com.enviro.assessment.junior.sellwane.service;

import com.enviro.assessment.junior.sellwane.dto.LoginRequest;
import com.enviro.assessment.junior.sellwane.dto.LoginResponse;
import com.enviro.assessment.junior.sellwane.exception.InvalidCredentialsException;
import com.enviro.assessment.junior.sellwane.model.Investor;
import com.enviro.assessment.junior.sellwane.repository.InvestorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final InvestorRepository investorRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(InvestorRepository investorRepository, PasswordEncoder passwordEncoder) {
        this.investorRepository = investorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest request) {
        Investor investor = investorRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), investor.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return new LoginResponse(investor.getId(), investor.getFullName(),
                investor.getEmail(), investor.getAge());
    }
}
