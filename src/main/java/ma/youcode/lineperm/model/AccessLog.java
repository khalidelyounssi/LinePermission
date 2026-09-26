package ma.youcode.lineperm.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class AccessLog {
    private final LocalDate date;
    private final LocalTime heure;
    private final String utilisateur;
    private final String action;
    private final String fichier;
    private final String resultat;



    public AccessLog(LocalDate date, LocalTime heure, String utilisateur, String action, String fichier, String resultat) {
        this.date = date;
        this.heure = heure;
        this.utilisateur = utilisateur;
        this.action = action;
        this.fichier = fichier;
        this.resultat = resultat;
    }



    public LocalDate getDate() {
        return date;
    }



    public LocalTime getHeure() {
        return heure;
    }



    public String getUtilisateur() {
        return utilisateur;
    }



    public String getAction() {
        return action;
    }



    public String getFichier() {
        return fichier;
    }



    public String getResultat() {
        return resultat;
    }



   
}
