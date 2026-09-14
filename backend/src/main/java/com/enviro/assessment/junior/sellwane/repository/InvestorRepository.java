package com.enviro.assessment.junior.sellwane.repository;

import com.enviro.assessment.junior.sellwane.model.Investor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvestorRepository extends JpaRepository<Investor, Long> {

    Optional<Investor> findByEmail(String email);
}
