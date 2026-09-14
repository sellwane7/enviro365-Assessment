package com.enviro.assessment.junior.sellwane.controller;

import com.enviro.assessment.junior.sellwane.model.Investor;
import com.enviro.assessment.junior.sellwane.service.InvestorService;
import org.springframework.web.bind.annotation.*;

/**
 * @RestController = @Controller + @ResponseBody combined: every method's
 * return value is automatically converted to JSON in the HTTP response body.
 *
 * @CrossOrigin allows our plain HTML/JS frontend (which runs from a
 * different origin - e.g. a local file or a different port) to call this
 * API from the browser without being blocked by the browser's CORS rules.
 */
@RestController
@RequestMapping("/api/investors")
@CrossOrigin(origins = "*")
public class InvestorController {

    private final InvestorService investorService;

    public InvestorController(InvestorService investorService) {
        this.investorService = investorService;
    }

    /**
     * GET /api/investors/{id}/portfolio
     * Returns the investor's details plus their list of products as JSON.
     * Powers the "Portfolio dashboard" screen on the frontend.
     */
    @GetMapping("/{id}/portfolio")
    public Investor getPortfolio(@PathVariable Long id) {
        return investorService.getPortfolio(id);
    }
}
