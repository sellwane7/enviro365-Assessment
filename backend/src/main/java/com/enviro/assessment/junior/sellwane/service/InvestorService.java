package com.enviro.assessment.junior.sellwane.service;

import com.enviro.assessment.junior.sellwane.exception.ResourceNotFoundException;
import com.enviro.assessment.junior.sellwane.model.Investor;
import com.enviro.assessment.junior.sellwane.repository.InvestorRepository;
import org.springframework.stereotype.Service;

/**
 * @Service marks this as a "business logic" bean that Spring creates and
 * injects wherever it's needed (e.g. into InvestorController).
 * Keeping this logic OUT of the controller keeps the controller thin -
 * it just handles the HTTP part, and this class handles the "what do we
 * actually do" part.
 */
@Service
public class InvestorService {

    private final InvestorRepository investorRepository;

    // Constructor injection - Spring automatically supplies the repository
    // when it creates this service.
    public InvestorService(InvestorRepository investorRepository) {
        this.investorRepository = investorRepository;
    }

    /**
     * Looks up an investor by ID, together with their products (portfolio).
     * If no investor exists with that ID, we throw ResourceNotFoundException,
     * which GlobalExceptionHandler turns into a clean 404 response.
     */
    public Investor getPortfolio(Long investorId) {
        return investorRepository.findById(investorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Investor not found with id: " + investorId));
    }
}
