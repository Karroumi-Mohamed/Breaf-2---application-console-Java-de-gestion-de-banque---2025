package org.bank.services;

import org.bank.enums.TypeTransaction;
import org.bank.enums.TypeCompte;
import org.bank.enums.Role;
import org.bank.models.*;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

public class BankService {
    private Map<String, Client> clients;
    private Map<String, Manager> managers;
    private Map<String, Account> accounts;
    private List<Transaction> transactions;

    // Session management
    private Personne loggedUser;
    private boolean isLoggedIn;

    public BankService() {
        this.clients = new HashMap<>();
        this.managers = new HashMap<>();
        this.accounts = new HashMap<>();
        this.transactions = new ArrayList<>();
        this.loggedUser = null;
        this.isLoggedIn = false;
    }

    public void deposit(Account account, double amount, String description) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        Transaction transaction = new Transaction(TypeTransaction.DEPOT, amount, description, account);
        account.setBalance(account.getBalance() + amount);
        account.addTransaction(transaction);
        transactions.add(transaction);
    }

    public void withdraw(Account account, double amount, String description) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (account.getBalance() < amount) {
            throw new ArithmeticException("Insufficient balance for withdrawal");
        }

        Transaction transaction = new Transaction(TypeTransaction.RETRAIT, amount, description, account);
        account.setBalance(account.getBalance() - amount);
        account.addTransaction(transaction);
        transactions.add(transaction);
    }

    public void transfer(Account sourceAccount, Account destinationAccount, double amount, String description) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        if (sourceAccount.getBalance() < amount) {
            throw new ArithmeticException("Insufficient balance for transfer");
        }
        if (sourceAccount.equals(destinationAccount)) {
            throw new IllegalStateException("Cannot transfer to the same account");
        }

        Transaction transaction = new Transaction(TypeTransaction.VIREMENT, amount, description, sourceAccount, destinationAccount);

        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);

        sourceAccount.addTransaction(transaction);
        destinationAccount.addTransaction(transaction);
        transactions.add(transaction);
    }

    public Client createClient(String nom, String prenom, String email, String motDePasse) {
        Client client = new Client(nom, prenom, email, motDePasse);
        clients.put(client.getClientId(), client);
        return client;
    }

    public Manager createManager(String nom, String prenom, String email, String motDePasse, String department) {
        Manager manager = new Manager(nom, prenom, email, motDePasse, department);
        managers.put(manager.getManagerId(), manager);
        return manager;
    }

    public Account createAccount(TypeCompte accountType, Client client) {
        Account account = new Account(accountType, client);
        client.addAccount(account);
        accounts.put(account.getAccountId(), account);
        return account;
    }

    public Optional<Client> findClientById(String clientId) {
        return Optional.ofNullable(clients.get(clientId));
    }

    public Optional<Client> findClientByEmail(String email) {
        return clients.values().stream()
                .filter(client -> client.getEmail().equals(email))
                .findFirst();
    }

    public Optional<Client> findClientByName(String name) {
        return clients.values().stream()
                .filter(client -> client.getPrenom().toLowerCase().contains(name.toLowerCase()) ||
                                client.getNom().toLowerCase().contains(name.toLowerCase()))
                .findFirst();
    }

    public Optional<Manager> findManagerById(String managerId) {
        return Optional.ofNullable(managers.get(managerId));
    }

    public Optional<Account> findAccountById(String accountId) {
        return Optional.ofNullable(accounts.get(accountId));
    }

    public List<Client> getAllClients() {
        return new ArrayList<>(clients.values());
    }

    public List<Manager> getAllManagers() {
        return new ArrayList<>(managers.values());
    }

    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    public double calculateTotalBalance(Client client) {
        return client.getAccounts().stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }

    public List<Transaction> getTransactionsByType(Client client, TypeTransaction type) {
        return client.getAccounts().stream()
                .flatMap(account -> account.getTransactions().stream())
                .filter(transaction -> transaction.getTransactionType() == type)
                .collect(Collectors.toList());
    }

    public List<Transaction> getSuspiciousTransactions(double threshold) {
        return transactions.stream()
                .filter(transaction -> transaction.getAmount() > threshold)
                .collect(Collectors.toList());
    }

    public List<Transaction> getClientTransactions(Client client) {
        return client.getAccounts().stream()
                .flatMap(account -> account.getTransactions().stream())
                .collect(Collectors.toList());
    }

    public double getTotalDeposits(Client client) {
        return getTransactionsByType(client, TypeTransaction.DEPOT).stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalWithdrawals(Client client) {
        return getTransactionsByType(client, TypeTransaction.RETRAIT).stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    // Session management methods
    public boolean login(String email, String password) {
        // Try to find as client first
        Optional<Client> clientOpt = clients.values().stream()
                .filter(client -> client.getEmail().equals(email) && client.getMotDePasse().equals(password))
                .findFirst();
        if (clientOpt.isPresent()) {
            loggedUser = clientOpt.get();
            isLoggedIn = true;
            return true;
        }

        // Try to find as manager
        Optional<Manager> managerOpt = managers.values().stream()
                .filter(manager -> manager.getEmail().equals(email) && manager.getMotDePasse().equals(password))
                .findFirst();
        if (managerOpt.isPresent()) {
            loggedUser = managerOpt.get();
            isLoggedIn = true;
            return true;
        }

        return false; // User not found or wrong password
    }

    public void logout() {
        loggedUser = null;
        isLoggedIn = false;
    }

    public Personne getLoggedUser() {
        return loggedUser;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public Role getCurrentUserRole() {
        if (!isLoggedIn || loggedUser == null) {
            return null;
        }
        return loggedUser.getRole();
    }

    public String getCurrentUserId() {
        if (!isLoggedIn || loggedUser == null) {
            return null;
        }

        if (loggedUser instanceof Client) {
            return ((Client) loggedUser).getClientId();
        } else if (loggedUser instanceof Manager) {
            return ((Manager) loggedUser).getManagerId();
        }

        return null;
    }
}