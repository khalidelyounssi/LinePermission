package ma.youcode.lineperm.dao;

import java.util.ArrayList;
import java.util.List;

import ma.youcode.lineperm.model.FichierProtege;

public class FichierDao extends AbstractDao<FichierProtege> {

    
    public boolean save(FichierProtege fichier ){

        return false;
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