package com.example.bank.model;

public class Account {

    private Long id;
    private String customerName;
    private String accountNumber;
    private double balance;

    public Account() {
    }

    public Account(Long id, String customerName,
                   String accountNumber, double balance) {
        this.id = id;
        this.customerName = customerName;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
