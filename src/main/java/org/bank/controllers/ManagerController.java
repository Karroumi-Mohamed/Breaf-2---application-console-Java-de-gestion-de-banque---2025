package org.bank.controllers;

import org.bank.enums.TypeCompte;
import org.bank.enums.TypeTransaction;
import org.bank.models.*;
import org.bank.services.BankService;
import org.bank.utils.ConsoleUtils;
import java.util.List;
import java.util.Optional;

public class ManagerController {
    private BankService bankService;

    public ManagerController(BankService bankService) {
        this.bankService = bankService;
    }

    public Manager registerManager() {
        try {
            String nom = ConsoleUtils.readString("Enter last name");
            String prenom = ConsoleUtils.readString("Enter first name");
            String email = ConsoleUtils.readEmail("Enter email");
            String password = ConsoleUtils.readString("Enter password");
            String department = ConsoleUtils.readString("Enter department");

            Manager manager = bankService.createManager(nom, prenom, email, password, department);
            System.out.println("Manager registered successfully!");
            System.out.println("Your Manager ID: " + manager.getManagerId());
            System.out.println("Please save this ID for login.");
            return manager;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public void createClient() {
        try {
            String nom = ConsoleUtils.readString("Enter client last name");
            String prenom = ConsoleUtils.readString("Enter client first name");
            String email = ConsoleUtils.readEmail("Enter client email");
            String password = ConsoleUtils.readString("Enter client password");

            Client client = bankService.createClient(nom, prenom, email, password);
            System.out.println("Client created successfully!");
            System.out.println("Client ID: " + client.getClientId());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void viewClient() {
        try {
            String clientName = ConsoleUtils.readString("Enter client first name or last name");
            Optional<Client> clientOpt = bankService.findClientByName(clientName);

            if (!clientOpt.isPresent()) {
                System.out.println("Client not found!");
                return;
            }

            Client client = clientOpt.get();
            System.out.println("\n=== CLIENT DETAILS ===");
            System.out.println("Name: " + client.getPrenom() + " " + client.getNom());
            System.out.println("Email: " + client.getEmail());
            System.out.println("Total Balance: " + bankService.calculateTotalBalance(client) + "€");

            System.out.println("\n=== CLIENT ACCOUNTS ===");
            for (Account account : client.getAccounts()) {
                System.out.println("- " + account.getAccountType() + ": " + account.getBalance() + "€");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void createAccount() {
        try {
            String clientName = ConsoleUtils.readString("Enter client first name or last name");
            Optional<Client> clientOpt = bankService.findClientByName(clientName);

            if (!clientOpt.isPresent()) {
                System.out.println("Client not found!");
                return;
            }

            Client client = clientOpt.get();
            String[] options = {"COURANT", "EPARGNE", "DEPOTATERME"};
            ConsoleUtils.displayMenu("Account Types", options);

            int choice = ConsoleUtils.readInt("Choose account type (1-3)");
            TypeCompte accountType;

            switch (choice) {
                case 1: accountType = TypeCompte.COURANT; break;
                case 2: accountType = TypeCompte.EPARGNE; break;
                case 3: accountType = TypeCompte.DEPOTATERME; break;
                default:
                    System.out.println("Invalid choice!");
                    return;
            }

            Account account = bankService.createAccount(accountType, client);
            System.out.println("Account created successfully!");
            System.out.println("Account ID: " + account.getAccountId());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void addDeposit() {
        try {
            String accountId = ConsoleUtils.readString("Enter account ID");
            Optional<Account> accountOpt = bankService.findAccountById(accountId);

            if (!accountOpt.isPresent()) {
                System.out.println("Account not found!");
                return;
            }

            Account account = accountOpt.get();
            double amount = ConsoleUtils.readDouble("Enter deposit amount");
            String description = ConsoleUtils.readString("Enter description");

            bankService.deposit(account, amount, description);
            System.out.println("Deposit added successfully!");
            System.out.println("New balance: " + account.getBalance() + "€");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void addWithdrawal() {
        try {
            String accountId = ConsoleUtils.readString("Enter account ID");
            Optional<Account> accountOpt = bankService.findAccountById(accountId);

            if (!accountOpt.isPresent()) {
                System.out.println("Account not found!");
                return;
            }

            Account account = accountOpt.get();
            double amount = ConsoleUtils.readDouble("Enter withdrawal amount");
            String description = ConsoleUtils.readString("Enter description");

            bankService.withdraw(account, amount, description);
            System.out.println("Withdrawal added successfully!");
            System.out.println("New balance: " + account.getBalance() + "€");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void addTransfer() {
        try {
            String sourceId = ConsoleUtils.readString("Enter source account ID");
            String destId = ConsoleUtils.readString("Enter destination account ID");

            Optional<Account> sourceOpt = bankService.findAccountById(sourceId);
            Optional<Account> destOpt = bankService.findAccountById(destId);

            if (!sourceOpt.isPresent()) {
                System.out.println("Source account not found!");
                return;
            }
            if (!destOpt.isPresent()) {
                System.out.println("Destination account not found!");
                return;
            }

            Account sourceAccount = sourceOpt.get();
            Account destAccount = destOpt.get();
            double amount = ConsoleUtils.readDouble("Enter transfer amount");
            String description = ConsoleUtils.readString("Enter description");

            bankService.transfer(sourceAccount, destAccount, amount, description);
            System.out.println("Transfer added successfully!");
            System.out.println("Source balance: " + sourceAccount.getBalance() + "€");
            System.out.println("Destination balance: " + destAccount.getBalance() + "€");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void viewAllClients() {
        try {
            List<Client> clients = bankService.getAllClients();
            if (clients.isEmpty()) {
                System.out.println("No clients found.");
                return;
            }

            System.out.println("\n=== ALL CLIENTS ===");
            for (Client client : clients) {
                System.out.println("Name: " + client.getPrenom() + " " + client.getNom() +
                    " | Email: " + client.getEmail() +
                    " | Accounts: " + client.getAccounts().size() +
                    " | Total Balance: " + bankService.calculateTotalBalance(client) + "€");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void viewClientTransactions() {
        try {
            String clientName = ConsoleUtils.readString("Enter client first name or last name");
            Optional<Client> clientOpt = bankService.findClientByName(clientName);

            if (!clientOpt.isPresent()) {
                System.out.println("Client not found!");
                return;
            }

            Client client = clientOpt.get();
            List<Transaction> transactions = bankService.getClientTransactions(client);

            if (transactions.isEmpty()) {
                System.out.println("No transactions found for this client.");
                return;
            }

            System.out.println("\n=== CLIENT TRANSACTIONS ===");
            for (Transaction transaction : transactions) {
                System.out.println(transaction.toString());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void viewSuspiciousTransactions() {
        try {
            double threshold = ConsoleUtils.readDouble("Enter suspicious amount threshold");
            List<Transaction> suspiciousTransactions = bankService.getSuspiciousTransactions(threshold);

            if (suspiciousTransactions.isEmpty()) {
                System.out.println("No suspicious transactions found above " + threshold + "€");
                return;
            }

            System.out.println("\n=== SUSPICIOUS TRANSACTIONS ===");
            for (Transaction transaction : suspiciousTransactions) {
                System.out.println(transaction.toString() +
                    " | Account: " + transaction.getSourceAccount().getAccountId());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void generateClientReport() {
        try {
            String clientName = ConsoleUtils.readString("Enter client first name or last name");
            Optional<Client> clientOpt = bankService.findClientByName(clientName);

            if (!clientOpt.isPresent()) {
                System.out.println("Client not found!");
                return;
            }

            Client client = clientOpt.get();

            System.out.println("\n=== CLIENT FINANCIAL REPORT ===");
            System.out.println("Client: " + client.getPrenom() + " " + client.getNom());
            System.out.println("Total Balance: " + bankService.calculateTotalBalance(client) + "€");
            System.out.println("Total Deposits: " + bankService.getTotalDeposits(client) + "€");
            System.out.println("Total Withdrawals: " + bankService.getTotalWithdrawals(client) + "€");

            System.out.println("\nTransactions by Type:");
            System.out.println("- Deposits: " + bankService.getTransactionsByType(client, TypeTransaction.DEPOT).size());
            System.out.println("- Withdrawals: " + bankService.getTransactionsByType(client, TypeTransaction.RETRAIT).size());
            System.out.println("- Transfers: " + bankService.getTransactionsByType(client, TypeTransaction.VIREMENT).size());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}