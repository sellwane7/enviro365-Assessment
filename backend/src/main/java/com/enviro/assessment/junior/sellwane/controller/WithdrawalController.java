package com.enviro.assessment.junior.sellwane.controller;

import com.enviro.assessment.junior.sellwane.dto.WithdrawalRequest;
import com.enviro.assessment.junior.sellwane.model.WithdrawalNotice;
import com.enviro.assessment.junior.sellwane.model.WithdrawalStatus;
import com.enviro.assessment.junior.sellwane.service.WithdrawalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/withdrawals")
@CrossOrigin(origins = "*")
public class WithdrawalController {

    private final WithdrawalService withdrawalService;

    public WithdrawalController(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }

    /**
     * POST /api/withdrawals
     * Body: { "productId": 1, "amount": 5000.00 }
     *
     * @Valid triggers the validation rules declared on WithdrawalRequest
     * (@NotNull, @DecimalMin - see that class). This is the "Input
     * validation" requirement: if the JSON is missing a field or the
     * amount is zero/negative, Spring rejects it BEFORE this method's
     * body even runs, and GlobalExceptionHandler turns that into a
     * clean 400 response with a helpful message.
     */
    @PostMapping
    public ResponseEntity<WithdrawalNotice> createWithdrawal(@Valid @RequestBody WithdrawalRequest request) {
        WithdrawalNotice notice = withdrawalService.createWithdrawal(request);
        return ResponseEntity.ok(notice);
    }

    /**
     * GET /api/withdrawals/investor/{investorId}
     * Returns the full withdrawal history for one investor (newest first).
     * Powers the "Withdrawal history table" on the frontend.
     */
    @GetMapping("/investor/{investorId}")
    public List<WithdrawalNotice> getHistory(@PathVariable Long investorId) {
        return withdrawalService.getHistory(investorId);
    }

    /**
     * GET /api/withdrawals/investor/{investorId}/export?status=APPROVED
     * "status" is an OPTIONAL query parameter (required = false) - this is
     * the "filtering" part of "Export CSV statements with filtering".
     * Leave it off to include every withdrawal for that investor.
     *
     * We return raw bytes with a "Content-Disposition: attachment" header,
     * which tells the browser to download the response as a .csv file
     * instead of trying to display it - this is what the "CSV download
     * button" on the frontend will call.
     */
    @GetMapping("/investor/{investorId}/export")
    public ResponseEntity<byte[]> exportCsv(
            @PathVariable Long investorId,
            @RequestParam(required = false) WithdrawalStatus status) {

        String csv = withdrawalService.exportHistoryAsCsv(investorId, status);
        byte[] csvBytes = csv.getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "withdrawal-statement-" + investorId + ".csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(csvBytes);
    }
}
