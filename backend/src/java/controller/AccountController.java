package com.example.bank.controller;

import com.example.bank.model.Account;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@CrossOrigin(origins = "*")
public class AccountController {

    private Account account = new Account(
            1L,
            "Demo User",
            "1234567890",
            25000.00
    );

    @GetMapping
    public Account getAccount() {
        return account;
    }

    @PostMapping("/deposit")
    public Account deposit(@RequestParam double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        account.setBalance(account.getBalance() + amount);

        return account;
    }

    @PostMapping("/withdraw")
    public Account withdraw(@RequestParam double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        if (amount > account.getBalance()) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        account.setBalance(account.getBalance() - amount);

        return account;
    }
}
