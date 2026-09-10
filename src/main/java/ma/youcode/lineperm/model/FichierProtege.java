package ma.youcode.lineperm.model;

public class FichierProtege {

    private final String nom;
    private final String owner;

    private boolean ownerRead;
    private boolean ownerWrite;
    private boolean ownerDelete;

    private boolean autresRead;
    private boolean autresWrite;
    private boolean autresDelete;

    public FichierProtege(String nom, String owner) {
        this.nom = nom;
        this.owner = owner;

        this.ownerRead = true;
        this.ownerWrite = true;
        this.ownerDelete = true;

        this.autresRead = false;
        this.autresWrite = false;
        this.autresDelete = false;
    }

    public FichierProtege(
            String nom,
            String owner,
            boolean ownerRead,
            boolean ownerWrite,
            boolean ownerDelete,
            boolean autresRead,
            boolean autresWrite,
            boolean autresDelete
    ) {
        this.nom = nom;
        this.owner = owner;

        this.ownerRead = ownerRead;
        this.ownerWrite = ownerWrite;
        this.ownerDelete = ownerDelete;

        this.autresRead = autresRead;
        this.autresWrite = autresWrite;
        this.autresDelete = autresDelete;
    }

    public String getNom() {
        return nom;
    }

    public String getOwner() {
        return owner;
    }

    public boolean isOwnerRead() {
        return ownerRead;
    }

    public boolean isOwnerWrite() {
        return ownerWrite;
    }

    public boolean isOwnerDelete() {
        return ownerDelete;
    }

    public boolean isAutresRead() {
        return autresRead;
    }

    public boolean isAutresWrite() {
        return autresWrite;
    }

    public boolean isAutresDelete() {
        return autresDelete;
    }

    public boolean grantPermission(char permission) {

        permission = Character.toLowerCase(permission);

        switch (permission) {
            case 'r':
                autresRead = true;
                return true;

            case 'w':
                autresWrite = true;
                return true;

            case 'd':
                autresDelete = true;
                return true;

            default:
                return false;
        }
    }

    public boolean removePermission(char permission) {

        permission = Character.toLowerCase(permission);

        switch (permission) {
            case 'r':
                autresRead = false;
                return true;

            case 'w':
                autresWrite = false;
                return true;

            case 'd':
                autresDelete = false;
                return true;

            default:
                return false;
        }
    }

    public String getPermissions() {

        String permissions = "";

        if (ownerRead) {
            permissions += "r";
        } else {
            permissions += "-";
        }

        if (ownerWrite) {
            permissions += "w";
        } else {
            permissions += "-";
        }

        if (ownerDelete) {
            permissions += "d";
        } else {
            permissions += "-";
        }

        permissions += "|";

        if (autresRead) {
            permissions += "r";
        } else {
            permissions += "-";
        }

        if (autresWrite) {
            permissions += "w";
        } else {
            permissions += "-";
        }

        if (autresDelete) {
            permissions += "d";
        } else {
            permissions += "-";
        }

        return permissions;
    }
}