package org.bank;

import org.bank.controllers.ClientController;
import org.bank.controllers.ManagerController;
import org.bank.data.SampleData;
import org.bank.enums.Role;
import org.bank.models.Manager;
import org.bank.services.BankService;
import org.bank.utils.ConsoleUtils;

public class BankApplication {
    private BankService bankService;
    private ClientController clientController;
    private ManagerController managerController;

    public BankApplication() {
        this.bankService = new BankService();
        this.clientController = new ClientController(bankService);
        this.managerController = new ManagerController(bankService);

        // Load sample data for testing
        SampleData.loadSampleData(bankService);
    }

    public void run() {
        System.out.println("=== WELCOME TO MOROCCAN BANK SYSTEM ===");

        while (true) {
            try {
                if (!bankService.isLoggedIn()) {
                    // Not logged in - show login menu
                    String[] mainOptions = {"Login", "Register as Manager", "Exit"};
                    ConsoleUtils.displayMenu("Main Menu", mainOptions);

                    int choice = ConsoleUtils.readInt("Choose option (1-3)");

                    switch (choice) {
                        case 1:
                            login();
                            break;
                        case 2:
                            registerManager();
                            break;
                        case 3:
                            System.out.println("Thank you for using Moroccan Bank System!");
                            return;
                        default:
                            System.out.println("Invalid choice! Please try again.");
                    }
                } else {
                    // Logged in - show role-based menu
                    showLoggedInMenu();
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void login() {
        String email = ConsoleUtils.readEmail("Enter your email");
        String password = ConsoleUtils.readString("Enter your password");

        if (bankService.login(email, password)) {
            System.out.println("Login successful!");
            System.out.println("Welcome, " + bankService.getLoggedUser().getPrenom() + " " + bankService.getLoggedUser().getNom());
        } else {
            System.out.println("Invalid email or password!");
        }
    }

    private void registerManager() {
        managerController.registerManager();
    }

    private void showLoggedInMenu() {
        Role userRole = bankService.getCurrentUserRole();

        if (userRole == Role.CLIENT) {
            showClientMenu();
        } else if (userRole == Role.MANAGER) {
            showManagerMenu();
        }
    }

    private void showClientMenu() {
        String userId = bankService.getCurrentUserId();

        String[] clientOptions = {
            "View My Info",
            "View My Transactions",
            "Filter My Transactions",
            "Sort My Transactions",
            "Calculate My Totals",
            "Logout"
        };

        ConsoleUtils.displayMenu("Client Menu", clientOptions);
        int choice = ConsoleUtils.readInt("Choose option (1-6)");

        switch (choice) {
            case 1:
                clientController.viewMyInfo(userId);
                break;
            case 2:
                clientController.viewMyTransactions(userId);
                break;
            case 3:
                clientController.filterMyTransactions(userId);
                break;
            case 4:
                clientController.sortMyTransactions(userId);
                break;
            case 5:
                clientController.calculateMyTotals(userId);
                break;
            case 6:
                bankService.logout();
                System.out.println("Logged out successfully!");
                break;
            default:
                System.out.println("Invalid choice! Please try again.");
        }
    }

    private void showManagerMenu() {
        String[] managerOptions = {
            "Create Client",
            "View Client",
            "Create Account",
            "Add Deposit",
            "Add Withdrawal",
            "Add Transfer",
            "View All Clients",
            "View Client Transactions",
            "View Suspicious Transactions",
            "Generate Client Report",
            "Logout"
        };

        ConsoleUtils.displayMenu("Manager Menu", managerOptions);
        int choice = ConsoleUtils.readInt("Choose option (1-11)");

        switch (choice) {
            case 1:
                managerController.createClient();
                break;
            case 2:
                managerController.viewClient();
                break;
            case 3:
                managerController.createAccount();
                break;
            case 4:
                managerController.addDeposit();
                break;
            case 5:
                managerController.addWithdrawal();
                break;
            case 6:
                managerController.addTransfer();
                break;
            case 7:
                managerController.viewAllClients();
                break;
            case 8:
                managerController.viewClientTransactions();
                break;
            case 9:
                managerController.viewSuspiciousTransactions();
                break;
            case 10:
                managerController.generateClientReport();
                break;
            case 11:
                bankService.logout();
                System.out.println("Logged out successfully!");
                break;
            default:
                System.out.println("Invalid choice! Please try again.");
        }
    }

    public static void main(String[] args) {
        new BankApplication().run();
    }
}