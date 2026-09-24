package ma.youcode.lineperm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
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



    public FichierProtege findById(int id){
 

        return null;
    }
    public boolean delete(int id){
        return false;
      }


    public List<FichierProtege>findByProprietaire(int userId){
        return new ArrayList<>();
    }


    public boolean updateDroits(int id,String droits){
        return false;
    }
}