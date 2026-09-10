package ma.youcode.lineperm.access;

import ma.youcode.lineperm.model.FichierProtege;
import ma.youcode.lineperm.model.User;

public class ControleAcces {

    public static boolean isOwner(
            User user,
            FichierProtege fichier
    ) {
        if (user == null || fichier == null) {
            return false;
        }

        return fichier.getOwner().equals(
                user.getLogin()
        );
    }

    public static boolean canAccess(
            User user,
            FichierProtege fichier,
            char permission
    ) {
        if (user == null || fichier == null) {
            return false;
        }

        permission =
                Character.toLowerCase(permission);

        if (isOwner(user, fichier)) {

            switch (permission) {
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

        switch (permission) {
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
}