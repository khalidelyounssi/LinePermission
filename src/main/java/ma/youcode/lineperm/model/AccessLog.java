package ma.youcode.lineperm.model;

public class AccessLog {
    private final String date;
    private final String heure;
    private final String utilisateur;
    private final String action;
    private final String fichier;
    private final String resultat;



    public AccessLog(String date, String heure, String utilisateur, String action, String fichier, String resultat) {
        this.date = date;
        this.heure = heure;
        this.utilisateur = utilisateur;
        this.action = action;
        this.fichier = fichier;
        this.resultat = resultat;
    }



    public String getDate() {
        return date;
    }



    public String getHeure() {
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
