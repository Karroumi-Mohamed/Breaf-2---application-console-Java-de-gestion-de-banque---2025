package org.bank.models;

import org.bank.enums.Role;
import java.util.ArrayList;
import java.util.List;

public class Client extends Personne {
    private static int nextId = 1;
    private int idClient;
    private List<Compte> comptes;

    public Client(String nom, String prenom, String email, String motDePasse) {
        super(nom, prenom, email, motDePasse);
        this.idClient = nextId++;
        this.comptes = new ArrayList<>();
    }

    @Override
    public Role getRole() {
        return Role.CLIENT;
    }

    public int getIdClient() {
        return idClient;
    }

    public List<Compte> getComptes() {
        return comptes;
    }

    public void ajouterCompte(Compte compte) {
        if (compte != null && !comptes.contains(compte)) {
            comptes.add(compte);
        }
    }

    public void supprimerCompte(Compte compte) {
        comptes.remove(compte);
    }

    @Override
    public String toString() {
        return String.format("[%d] %s - %d compte(s)", idClient, super.toString(), comptes.size());
    }
}