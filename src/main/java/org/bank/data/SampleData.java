package org.bank.data;

import org.bank.enums.TypeCompte;
import org.bank.models.Account;
import org.bank.models.Client;
import org.bank.models.Manager;
import org.bank.services.BankService;

public class SampleData {

    public static void loadSampleData(BankService bankService) {
        // Create sample clients
        Client client1 = bankService.createClient("Alami", "Ahmed", "ahmed.alami@email.com", "password123");
        Client client2 = bankService.createClient("Benjelloun", "Fatima", "fatima.benjelloun@email.com", "password456");
        Client client3 = bankService.createClient("Tazi", "Omar", "omar.tazi@email.com", "password789");

        // Create sample manager
        Manager manager1 = bankService.createManager("Idrissi", "Youssef", "youssef.idrissi@bank.com", "admin123", "Operations");

        // Create sample accounts
        Account account1 = bankService.createAccount(TypeCompte.COURANT, client1);
        Account account2 = bankService.createAccount(TypeCompte.EPARGNE, client1);
        Account account3 = bankService.createAccount(TypeCompte.COURANT, client2);
        Account account4 = bankService.createAccount(TypeCompte.DEPOTATERME, client3);

        // Add sample transactions
        try {
            bankService.deposit(account1, 1000.0, "Initial deposit");
            bankService.deposit(account2, 5000.0, "Savings deposit");
            bankService.deposit(account3, 2000.0, "Salary deposit");
            bankService.deposit(account4, 10000.0, "Term deposit");

            bankService.withdraw(account1, 200.0, "ATM withdrawal");
            bankService.withdraw(account3, 500.0, "Cash withdrawal");

            bankService.transfer(account1, account3, 300.0, "Transfer to friend");
            bankService.transfer(account2, account1, 1000.0, "From savings to current");

            // Add some high-value transactions for suspicious detection
            bankService.deposit(account1, 15000.0, "Large deposit");
            bankService.withdraw(account2, 3000.0, "Large withdrawal");

        } catch (Exception e) {
            System.out.println("Error loading sample data: " + e.getMessage());
        }

        System.out.println("Sample data loaded successfully!");
        System.out.println("\n=== SAMPLE LOGIN CREDENTIALS ===");
        System.out.println("Clients:");
        System.out.println("- Email: ahmed.alami@email.com | Password: password123 (Ahmed Alami)");
        System.out.println("- Email: fatima.benjelloun@email.com | Password: password456 (Fatima Benjelloun)");
        System.out.println("- Email: omar.tazi@email.com | Password: password789 (Omar Tazi)");
        System.out.println("\nManager:");
        System.out.println("- Email: youssef.idrissi@bank.com | Password: admin123 (Youssef Idrissi)");
        System.out.println("\nFor manager operations:");
        System.out.println("- Use client names: Ahmed, Fatima, Omar");
        System.out.println("- Account IDs available when needed for transactions");
    }
}