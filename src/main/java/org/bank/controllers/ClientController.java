package org.bank.controllers;

import org.bank.enums.TypeTransaction;
import org.bank.models.Account;
import org.bank.models.Client;
import org.bank.models.Transaction;
import org.bank.services.BankService;
import org.bank.utils.ConsoleUtils;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Comparator;

public class ClientController {
    private BankService bankService;

    public ClientController(BankService bankService) {
        this.bankService = bankService;
    }

    public void viewMyInfo(String clientId) {
        try {
            Optional<Client> clientOpt = bankService.findClientById(clientId);
            if (!clientOpt.isPresent()) {
                System.out.println("Client not found!");
                return;
            }

            Client client = clientOpt.get();
            System.out.println("\n=== MY PERSONAL INFO ===");
            System.out.println("Name: " + client.getPrenom() + " " + client.getNom());
            System.out.println("Email: " + client.getEmail());
            System.out.println("Total Balance: " + bankService.calculateTotalBalance(client) + "€");

            System.out.println("\n=== MY ACCOUNTS ===");
            for (Account account : client.getAccounts()) {
                System.out.println("- " + account.getAccountType() + ": " + account.getBalance() + "€");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void viewMyTransactions(String clientId) {
        try {
            Optional<Client> clientOpt = bankService.findClientById(clientId);
            if (!clientOpt.isPresent()) {
                System.out.println("Client not found!");
                return;
            }

            Client client = clientOpt.get();
            List<Transaction> transactions = bankService.getClientTransactions(client);

            if (transactions.isEmpty()) {
                System.out.println("No transactions found.");
                return;
            }

            System.out.println("\n=== MY TRANSACTION HISTORY ===");
            for (Transaction transaction : transactions) {
                System.out.println(transaction.toString());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void filterMyTransactions(String clientId) {
        try {
            Optional<Client> clientOpt = bankService.findClientById(clientId);
            if (!clientOpt.isPresent()) {
                System.out.println("Client not found!");
                return;
            }

            Client client = clientOpt.get();

            String[] filterOptions = {"DEPOT", "RETRAIT", "VIREMENT", "ALL"};
            ConsoleUtils.displayMenu("Filter by Transaction Type", filterOptions);

            int choice = ConsoleUtils.readInt("Choose filter (1-4)");
            List<Transaction> transactions;

            switch (choice) {
                case 1:
                    transactions = bankService.getTransactionsByType(client, TypeTransaction.DEPOT);
                    break;
                case 2:
                    transactions = bankService.getTransactionsByType(client, TypeTransaction.RETRAIT);
                    break;
                case 3:
                    transactions = bankService.getTransactionsByType(client, TypeTransaction.VIREMENT);
                    break;
                case 4:
                    transactions = bankService.getClientTransactions(client);
                    break;
                default:
                    System.out.println("Invalid choice!");
                    return;
            }

            if (transactions.isEmpty()) {
                System.out.println("No transactions found for this filter.");
                return;
            }

            System.out.println("\n=== FILTERED TRANSACTIONS ===");
            for (Transaction transaction : transactions) {
                System.out.println(transaction.toString());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void calculateMyTotals(String clientId) {
        try {
            Optional<Client> clientOpt = bankService.findClientById(clientId);
            if (!clientOpt.isPresent()) {
                System.out.println("Client not found!");
                return;
            }

            Client client = clientOpt.get();

            System.out.println("\n=== MY FINANCIAL SUMMARY ===");
            System.out.println("Total Balance: " + bankService.calculateTotalBalance(client) + "€");
            System.out.println("Total Deposits: " + bankService.getTotalDeposits(client) + "€");
            System.out.println("Total Withdrawals: " + bankService.getTotalWithdrawals(client) + "€");

            List<Transaction> allTransactions = bankService.getClientTransactions(client);
            System.out.println("Total Transactions: " + allTransactions.size());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void sortMyTransactions(String clientId) {
        try {
            Optional<Client> clientOpt = bankService.findClientById(clientId);
            if (!clientOpt.isPresent()) {
                System.out.println("Client not found!");
                return;
            }

            Client client = clientOpt.get();
            List<Transaction> transactions = bankService.getClientTransactions(client);

            if (transactions.isEmpty()) {
                System.out.println("No transactions found.");
                return;
            }

            String[] sortOptions = {"Date (newest first)", "Date (oldest first)", "Amount (highest first)", "Amount (lowest first)"};
            ConsoleUtils.displayMenu("Sort Transactions", sortOptions);

            int choice = ConsoleUtils.readInt("Choose sort option (1-4)");

            switch (choice) {
                case 1:
                    transactions.sort(Comparator.comparing(Transaction::getDate).reversed());
                    break;
                case 2:
                    transactions.sort(Comparator.comparing(Transaction::getDate));
                    break;
                case 3:
                    transactions.sort(Comparator.comparing(Transaction::getAmount).reversed());
                    break;
                case 4:
                    transactions.sort(Comparator.comparing(Transaction::getAmount));
                    break;
                default:
                    System.out.println("Invalid choice!");
                    return;
            }

            System.out.println("\n=== SORTED TRANSACTIONS ===");
            for (Transaction transaction : transactions) {
                System.out.println(transaction.toString());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}