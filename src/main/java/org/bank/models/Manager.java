package org.bank.models;

import org.bank.enums.Role;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Manager extends Personne {
    private String managerId;
    private String department;
    private List<Client> clientList;

    public Manager(String nom, String prenom, String email, String motDePasse, String department) {
        super(nom, prenom, email, motDePasse);
        this.managerId = UUID.randomUUID().toString();
        this.department = department;
        this.clientList = new ArrayList<>();
    }

    @Override
    public Role getRole() {
        return Role.MANAGER;
    }

    public String getManagerId() {
        return managerId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<Client> getClientList() {
        return clientList;
    }

    public void addClient(Client client) {
        if (client != null && !clientList.contains(client)) {
            clientList.add(client);
        }
    }

    public void removeClient(Client client) {
        clientList.remove(client);
    }
}