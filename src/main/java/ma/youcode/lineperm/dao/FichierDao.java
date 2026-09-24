package ma.youcode.lineperm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ma.youcode.lineperm.model.FichierProtege;

public class FichierDao extends AbstractDao<FichierProtege> {

    
    public boolean save(FichierProtege fichier) {

        if (fichier == null) {
            return false;
        }

        String sql = "INSERT INTO fichiers (nom, owner_id, droits) VALUES (?, (SELECT id FROM users WHERE login = ?), ?)";

        try (
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1,fichier.getNom());
            statement.setString(2,fichier.getOwner());
            statement.setString(3,fichier.getPermissions());

            int lignesAjoutees = statement.executeUpdate();

            return lignesAjoutees == 1;

        } catch (SQLException e) {
            System.out.println("Erreur pendant l'ajout du fichier : " + e.getMessage());
            return false;
        }
    }



    
    public FichierProtege findById(int id) {

        if (id <= 0) {
            return null;
        }

        String sql = "SELECT f.nom, u.login AS owner, f.droits FROM fichiers f JOIN users u ON u.id = f.owner_id WHERE f.id = ?";

        try (
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1,id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return createFichier(resultSet);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur pendant la recherche du fichier : " + e.getMessage());
        }

        return null;
    }





    public FichierProtege findByName(String nom) {

        if (nom == null) {
            return null;
        }

        nom = nom.trim();

        if (nom.isEmpty()) {
            return null;
        }

        String sql = "SELECT f.nom, u.login AS owner, f.droits FROM fichiers f JOIN users u ON u.id = f.owner_id WHERE f.nom = ?";

        try (
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1,nom);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return createFichier(resultSet);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur pendant la recherche du fichier : " + e.getMessage());
        }

        return null;
    }



    public List<FichierProtege> findAll() {

        List<FichierProtege> fichiers = new ArrayList<>();

        String sql = "SELECT f.nom, u.login AS owner, f.droits FROM fichiers f JOIN users u ON u.id = f.owner_id ORDER BY f.nom";

        try (
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {
                FichierProtege fichier = createFichier(resultSet);

                if (fichier != null) {
                    fichiers.add(fichier);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur pendant la récupération des fichiers : " + e.getMessage());
        }

        return fichiers;
    }





    public List<FichierProtege> findByProprietaire(int userId) {

        List<FichierProtege> fichiers = new ArrayList<>();

        if (userId <= 0) {
            return fichiers;
        }

        String sql = "SELECT f.nom, u.login AS owner, f.droits FROM fichiers f JOIN users u ON u.id = f.owner_id WHERE f.owner_id = ? ORDER BY f.nom";

        try (
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1,userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    FichierProtege fichier = createFichier(resultSet);

                    if (fichier != null) {
                        fichiers.add(fichier);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur pendant la recherche des fichiers : " + e.getMessage());
        }

        return fichiers;
    }




    
    public boolean delete(int id) {

        if (id <= 0) {
            return false;
        }

        String sql = "DELETE FROM fichiers WHERE id = ?";

        try (
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1,id);

            int lignesSupprimees = statement.executeUpdate();

            return lignesSupprimees == 1;

        } catch (SQLException e) {
            System.out.println("erreur pendant la suppression du fichier : " + e.getMessage());
            return false;
        }
    }


    

    public boolean deleteByName(String nom) {

        if (nom == null) {
            return false;
        }

        nom = nom.trim();

        if (nom.isEmpty()) {
            return false;
        }

        String sql = "DELETE FROM fichiers WHERE nom = ?";

        try (
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1,nom);

            int lignesSupprimees = statement.executeUpdate();

            return lignesSupprimees == 1;

        } catch (SQLException e) {
            System.out.println("erreur pendant la suppression du fichier : " + e.getMessage());
            return false;
        }
    }

    public boolean updateDroits(int id,String droits) {

        if (id <= 0 || !droitsValides(droits)) {
            return false;
        }

        String sql = "UPDATE fichiers SET droits = ? WHERE id = ?";

        try (
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1,droits);
            statement.setInt(2,id);

            int lignesModifiees = statement.executeUpdate();

            return lignesModifiees == 1;

        } catch (SQLException e) {
            System.out.println("erreur pendant la modification des droits : " + e.getMessage());
            return false;
        }
    }




    public boolean updateDroitsByName(String nom,String droits) {

        if (nom == null || !droitsValides(droits)) {
            return false;
        }

        nom = nom.trim();

        if (nom.isEmpty()) {
            return false;
        }

        String sql = "UPDATE fichiers SET droits = ? WHERE nom = ?";

        try (
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1,droits);
            statement.setString(2,nom);

            int lignesModifiees = statement.executeUpdate();

            return lignesModifiees == 1;

        } catch (SQLException e) {
            System.out.println("Erreur pendant la modification des droits : " + e.getMessage());
            return false;
        }
    }




    private FichierProtege createFichier(ResultSet resultSet) throws SQLException {

        String nom = resultSet.getString("nom");
        String owner = resultSet.getString("owner");
        String droits = resultSet.getString("droits");

        if (!droitsValides(droits)) {
            return null;
        }

        boolean ownerRead = droits.charAt(0) == 'r';
        boolean ownerWrite = droits.charAt(1) == 'w';
        boolean ownerDelete = droits.charAt(2) == 'd';

        boolean autresRead = droits.charAt(4) == 'r';
        boolean autresWrite = droits.charAt(5) == 'w';
        boolean autresDelete = droits.charAt(6) == 'd';

        return new FichierProtege(
            nom,
            owner,
            ownerRead,
            ownerWrite,
            ownerDelete,
            autresRead,
            autresWrite,
            autresDelete
        );
    }



    private boolean droitsValides(String droits) {

        if (droits == null) {
            return false;
        }

        if (droits.length() != 7) {
            return false;
        }

        return droits.charAt(3) == '|';
    }
}