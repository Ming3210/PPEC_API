package com.ra.base_spring_boot.controller;


import com.ra.base_spring_boot.model.Withdrawal;
import com.ra.base_spring_boot.repository.WithdrawalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/withdrawals")
public class WithdrawalController {

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @PostMapping
    public ResponseEntity<Withdrawal> createWithdrawal(@RequestBody Withdrawal withdrawal) {
        Withdrawal saved = withdrawalRepository.save(withdrawal);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<Page<Withdrawal>> getWithdrawals(
            @RequestParam(required = false) String bankName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("withdrawalDate").descending());
        Page<Withdrawal> result = withdrawalRepository.searchWithdrawals(bankName, startDate, endDate, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Withdrawal> getWithdrawalById(@PathVariable Long id) {
        Optional<Withdrawal> withdrawal = withdrawalRepository.findById(id);
        return withdrawal.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWithdrawal(@PathVariable Long id) {
        if (!withdrawalRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        withdrawalRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
