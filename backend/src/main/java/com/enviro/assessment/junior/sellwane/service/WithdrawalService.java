package com.enviro.assessment.junior.sellwane.service;

import com.enviro.assessment.junior.sellwane.dto.WithdrawalRequest;
import com.enviro.assessment.junior.sellwane.exception.BusinessRuleException;
import com.enviro.assessment.junior.sellwane.exception.ResourceNotFoundException;
import com.enviro.assessment.junior.sellwane.model.*;
import com.enviro.assessment.junior.sellwane.repository.ProductRepository;
import com.enviro.assessment.junior.sellwane.repository.WithdrawalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * This is where all the business rules for withdrawals live. Keeping every
 * rule in one place (instead of scattered across the controller) means:
 *   - the controller stays a thin "translator" between HTTP and this class
 *   - we can read the whole withdrawal policy top-to-bottom in one method
 */
@Service
public class WithdrawalService {

    // 90% expressed as a BigDecimal so all our money math stays precise
    // (never use double/float for currency - it causes rounding errors)
    private static final BigDecimal MAX_WITHDRAWAL_PERCENTAGE = new BigDecimal("0.90");
    private static final int RETIREMENT_AGE_THRESHOLD = 65;

    private final WithdrawalRepository withdrawalRepository;
    private final ProductRepository productRepository;

    public WithdrawalService(WithdrawalRepository withdrawalRepository, ProductRepository productRepository) {
        this.withdrawalRepository = withdrawalRepository;
        this.productRepository = productRepository;
    }

    /**
     * Creates a withdrawal notice after validating every business rule, in order:
     *   1. Retirement products: investor must be older than 65
     *   2. Amount must not exceed the current balance
     *   3. Amount must not exceed 90% of the current balance
     * If any rule fails, we throw BusinessRuleException immediately and
     * nothing is saved. If all rules pass, we deduct the balance and save
     * a record of the withdrawal.
     *
     * @Transactional means: if anything fails partway through this method,
     * any database changes made so far are automatically rolled back - we
     * never end up with a half-saved, inconsistent state (e.g. balance
     * updated but no withdrawal record saved).
     */
    @Transactional
    public WithdrawalNotice createWithdrawal(WithdrawalRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + request.getProductId()));

        BigDecimal amount = request.getAmount();
        BigDecimal balanceBefore = product.getBalance();

        // Rule 1: Retirement withdrawals only allowed if investor age > 65
        if (product.getProductType().isRetirementProduct()) {
            int age = product.getInvestor().getAge();
            if (age <= RETIREMENT_AGE_THRESHOLD) {
                throw new BusinessRuleException(
                        "Retirement withdrawals are only allowed for investors older than "
                                + RETIREMENT_AGE_THRESHOLD + ". Investor is " + age + ".");
            }
        }

        // Rule 2: Withdrawal must not exceed balance
        if (amount.compareTo(balanceBefore) > 0) {
            throw new BusinessRuleException(
                    "Withdrawal amount (" + amount + ") exceeds available balance (" + balanceBefore + ").");
        }

        // Rule 3: Withdrawal must not exceed 90% of balance
        BigDecimal maxAllowed = balanceBefore.multiply(MAX_WITHDRAWAL_PERCENTAGE)
                .setScale(2, RoundingMode.HALF_UP);
        if (amount.compareTo(maxAllowed) > 0) {
            throw new BusinessRuleException(
                    "Withdrawal amount (" + amount + ") exceeds the maximum allowed of 90% of balance ("
                            + maxAllowed + ").");
        }

        // All rules passed - perform the balance calculation and persist the notice
        BigDecimal balanceAfter = balanceBefore.subtract(amount);
        product.setBalance(balanceAfter);
        productRepository.save(product);

        WithdrawalNotice notice = new WithdrawalNotice(
                product, amount, balanceBefore, balanceAfter, LocalDateTime.now(), WithdrawalStatus.APPROVED);
        return withdrawalRepository.save(notice);
    }

    /** Returns the full withdrawal history for an investor, newest first. */
    public List<WithdrawalNotice> getHistory(Long investorId) {
        return withdrawalRepository.findByProduct_Investor_IdOrderByRequestDateDesc(investorId);
    }

    /**
     * Builds a CSV file (as plain text) of an investor's withdrawal history.
     * "statusFilter" is optional - pass null to include every withdrawal.
     * This is the "Export CSV statements with filtering" requirement.
     */
    public String exportHistoryAsCsv(Long investorId, WithdrawalStatus statusFilter) {
        List<WithdrawalNotice> notices = withdrawalRepository
                .findByProduct_Investor_IdOrderByRequestDateDesc(investorId);

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        StringBuilder csv = new StringBuilder();
        csv.append("Withdrawal ID,Product,Amount Requested,Balance Before,Balance After,Date,Status\n");

        for (WithdrawalNotice n : notices) {
            if (statusFilter != null && n.getStatus() != statusFilter) {
                continue; // skip rows that don't match the requested filter
            }
            csv.append(n.getId()).append(",")
                    .append(escapeCsv(n.getProduct().getProductName())).append(",")
                    .append(n.getAmountRequested()).append(",")
                    .append(n.getBalanceBefore()).append(",")
                    .append(n.getBalanceAfter()).append(",")
                    .append(n.getRequestDate().format(dateFormat)).append(",")
                    .append(n.getStatus())
                    .append("\n");
        }

        return csv.toString();
    }

    /** Wraps a value in quotes if it contains a comma, so the CSV doesn't break. */
    private String escapeCsv(String value) {
        if (value != null && value.contains(",")) {
            return "\"" + value + "\"";
        }
        return value;
    }
}
