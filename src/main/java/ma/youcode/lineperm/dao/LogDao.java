package ma.youcode.lineperm.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ma.youcode.lineperm.model.AccessLog;

public class LogDao extends AbstractDao<AccessLog> {

   
    public boolean save(AccessLog log){
        return false;
    }

   
    public AccessLog findById(int id){
        return null;
    }

   
    public boolean delete(int id){

        return false;
    }

    public long compterTotal(){
 return 0;
    }

    public long compterRefuses(){
      return 0;
    }

    public List<String> userDistincts(){

        return new ArrayList<>();
    }

    public Map<String, Long> actionsByUser(){

        return new HashMap<>();
    }

    public List<Map.Entry<String, Long>>topFichiers(int limite){

        return new ArrayList<>();
    }

    public List<AccessLog> refusesByUser(String username){

        return new ArrayList<>();
    }

    public Optional<String> userPlusActif(){

        return Optional.empty();
    }

    public Map<String, Long>repartitionByAction(){

        return new HashMap<>();
    }
}