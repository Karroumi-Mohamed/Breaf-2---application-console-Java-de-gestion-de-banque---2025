package org.bank.models;

import org.bank.enums.TypeCompte;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Account {
    private String accountId;
    private TypeCompte accountType;
    private double balance;
    private List<Transaction> transactions;
    private Client client;

    public Account(TypeCompte accountType, Client client) {
        this.accountId = UUID.randomUUID().toString();
        this.accountType = accountType;
        this.balance = 0.0;
        this.client = client;
        this.transactions = new ArrayList<>();
    }

    public String getAccountId() {
        return accountId;
    }

    public TypeCompte getAccountType() {
        return accountType;
    }

    public void setAccountType(TypeCompte accountType) {
        this.accountType = accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void addTransaction(Transaction transaction) {
        if (transaction != null) {
            transactions.add(transaction);
        }
    }

    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
    }
}