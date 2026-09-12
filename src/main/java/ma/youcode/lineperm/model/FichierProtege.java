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

    private String contenu;


    


    public FichierProtege(String nom, String owner) {
    this(
        nom,
        owner,
        true,
        true,
        true,
        false,
        false,
        false
    );
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

    public void setOwnerRead(boolean ownerRead) {
        this.ownerRead = ownerRead;
    }

    public boolean isOwnerWrite() {
        return ownerWrite;
    }

    public void setOwnerWrite(boolean ownerWrite) {
        this.ownerWrite = ownerWrite;
    }

    public boolean isOwnerDelete() {
        return ownerDelete;
    }

    public void setOwnerDelete(boolean ownerDelete) {
        this.ownerDelete = ownerDelete;
    }

    public boolean isAutresRead() {
        return autresRead;
    }

    public void setAutresRead(boolean autresRead) {
        this.autresRead = autresRead;
    }

    public boolean isAutresWrite() {
        return autresWrite;
    }

    public void setAutresWrite(boolean autresWrite) {
        this.autresWrite = autresWrite;
    }

    public boolean isAutresDelete() {
        return autresDelete;
    }

    public void setAutresDelete(boolean autresDelete) {
        this.autresDelete = autresDelete;
    }

    public String getContenu() {
        return contenu;
    }


    public void setContenu(String contenu) {
        this.contenu = contenu;
    }


    public boolean grantP(char p) {

        p = Character.toLowerCase(p);

        switch (p) {

            case 'r':
                autresRead = true;
                return true;

            case 'w':
                autresWrite = true;
                autresRead = true;
                return true;

            case 'd':
                autresDelete = true;
                return true;

            default:
                return false;
        }
    }


    public boolean removeP(char p) {

        p = Character.toLowerCase(p);

        switch (p) {

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
            permissions = permissions + "r";
        } else {
            permissions = permissions + "-";
        }

        if (ownerWrite) {
            permissions = permissions + "w";
        } else {
            permissions = permissions + "-";
        }

        if (ownerDelete) {
            permissions = permissions + "d";
        } else {
            permissions = permissions + "-";
        }

        permissions = permissions + "|";

        if (autresRead) {
            permissions = permissions + "r";
        } else {
            permissions = permissions + "-";
        }

        if (autresWrite) {
            permissions = permissions + "w";
        } else {
            permissions = permissions + "-";
        }

        if (autresDelete) {
            permissions = permissions + "d";
        } else {
            permissions = permissions + "-";
        }

        return permissions;
    }
}
