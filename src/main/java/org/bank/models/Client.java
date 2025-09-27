package org.bank.models;

import org.bank.enums.Role;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Client extends Personne {
    private String clientId;
    private List<Account> accounts;

    public Client(String nom, String prenom, String email, String motDePasse) {
        super(nom, prenom, email, motDePasse);
        this.clientId = UUID.randomUUID().toString();
        this.accounts = new ArrayList<>();
    }

    @Override
    public Role getRole() {
        return Role.CLIENT;
    }

    public String getClientId() {
        return clientId;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        if (account != null && !accounts.contains(account)) {
            accounts.add(account);
        }
    }

    public void removeAccount(Account account) {
        accounts.remove(account);
    }
}