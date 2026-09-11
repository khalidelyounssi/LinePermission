package ma.youcode.lineperm.access;

import ma.youcode.lineperm.model.FichierProtege;

public class ControleAcces {


    public static boolean isOwner(String login, FichierProtege fichier) {

        if (login == null || fichier == null) {
                       return false;
        }

        if (fichier.getOwner().equals(login)) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean hasOwnerP(FichierProtege fichier, char p) {

        if (fichier == null) {
            return false;
        }

        p = Character.toLowerCase(p);

        switch (p) {

            case 'r':
                return fichier.isOwnerRead();

            case 'w':
                return fichier.isOwnerWrite();

            case 'd':
                return fichier.isOwnerDelete();

            default:
                return false;
        }
    }


    public static boolean hasAutresP(FichierProtege fichier, char p) {

        if (fichier == null) {
            return false;
        }

        p = Character.toLowerCase(p);

        switch (p) {

            case 'r':
                return fichier.isAutresRead();

            case 'w':
                return fichier.isAutresWrite();

            case 'd':
                return fichier.isAutresDelete();

            default:
                return false;
        }
    }


    public static boolean canAccess(String login, FichierProtege fichier, char p) {

        if (login == null || fichier == null) {
            return false;
        }

        login = login.trim();
        p = Character.toLowerCase(p);

        if (isOwner(login, fichier)) {
            return hasOwnerP(fichier, p);
        } else {
            return hasAutresP(fichier, p);
        }
    }
}