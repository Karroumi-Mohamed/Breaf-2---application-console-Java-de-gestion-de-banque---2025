package org.bank.models;

import org.bank.enums.TypeTransaction;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private String transactionId;
    private TypeTransaction transactionType;
    private double amount;
    private LocalDateTime date;
    private String description;
    private Account sourceAccount;
    private Account destinationAccount;

    public Transaction(TypeTransaction transactionType, double amount, String description, Account sourceAccount) {
        this(transactionType, amount, description, sourceAccount, null);
    }

    public Transaction(TypeTransaction transactionType, double amount, String description, Account sourceAccount, Account destinationAccount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.transactionId = UUID.randomUUID().toString();
        this.transactionType = transactionType;
        this.amount = amount;
        this.description = description;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.date = LocalDateTime.now();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public TypeTransaction getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TypeTransaction transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(Account sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public Account getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(Account destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    @Override
    public String toString() {
        return String.format("%s - %.2f€ - %s - %s",
            transactionType, amount,
            date.toLocalDate(), description);
    }
}