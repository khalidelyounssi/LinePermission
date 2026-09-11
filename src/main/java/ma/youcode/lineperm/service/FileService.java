package ma.youcode.lineperm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.youcode.lineperm.access.ControleAcces;
import ma.youcode.lineperm.model.FichierProtege;

public class FileService {

    private final Map<String, FichierProtege> fichiers = new HashMap<>();


    public boolean isValidName(String name) {

        if (name == null) {
            return false;
        }

        name = name.trim();

        if (name.isEmpty()) {
            return false;
        }

        if (name.contains("/")||name.contains("..")||name.contains("\\")) {
            return false;
        }

        return true;
    }


    public boolean createFile(String name, String owner) {

        if (!isValidName(name)) {
            return false;
        }

        name = name.trim();

        if (owner == null) {
            return false;
        }

        owner = owner.trim();

        if (owner.isEmpty()) {
            return false;
        }

        if (fichiers.containsKey(name)) {
            return false;
        }

        FichierProtege fiche = new FichierProtege(name, owner);

        fichiers.put(name, fiche);

        return true;
    }


    public List<String> listFiles() {

        List<String> lines = new ArrayList<>();

        for (FichierProtege fichier : fichiers.values()) {

            String line = fichier.getPermissions()+" "+fichier.getOwner()+" "+fichier.getNom();

            lines.add(line);
        }

        return lines;
    }
    public FichierProtege findFile(String name){
        if (name == null ){
            return null;
        }
        name = name.trim();
        if(name.isEmpty()){
            return null;
        }
            return fichiers.get(name);
        

    }

    public boolean deleteFile(String name , String login){
        if(name ==null||login== null){
            return false;
        }
        name = name.trim();

        FichierProtege file = findFile(name);

        if(file==null){
            return false;
        }

        if(!ControleAcces.canAccess(login, file, 'd')){
            return false;
        }
        else{
            fichiers.remove(name);
            return true;
        }
    }
    public boolean canReadFile(String name, String login){
         if(name ==null||login== null){
            return false;
        }
        name = name.trim();

        FichierProtege file = findFile(name);

        if(file==null){
            return false;
        }
        if(!ControleAcces.canAccess(login, file, 'r')){
            return false;
        }
        return true;
            
        


    }
    public boolean canWriteFile(String name, String login){
        if(name ==null||login== null){
            return false;
        }
        name = name.trim();

        FichierProtege file = findFile(name);

        if(file==null){
            return false;
        }
        if(!ControleAcces.canAccess(login, file, 'w')){
            return false;
        }
        return true;

    }

    public boolean writeFile(String name, String login, String contenu){
        if(name ==null||login== null||contenu==null){
            return false;
        }
        name = name.trim();

        FichierProtege file = findFile(name);

        if(file==null){
            return false;
        }
        if(!ControleAcces.canAccess(login, file, 'w')){
            return false;
        }else{
            file.setContenu(contenu);
            return true;
        }

    }
    public String readFile(String name, String login){
        if(name ==null||login== null){
            return null;
        }
        name = name.trim();

        FichierProtege file = findFile(name);

        if(file==null){
            return null;
        }
        if(!ControleAcces.canAccess(login, file, 'r')){
            return null;
        }else{
           return  file.getContenu();
        }
        
    }
    public boolean grantPermission(String name,String login,char p){

        if(name ==null||login== null){
            return false;
        }
        name = name.trim();

        FichierProtege file = findFile(name);

        if(file==null){
            return false;
        }
        if(!ControleAcces.isOwner(login, file)){
            return  false;
        }
        return file.grantP(p);

    }
    public boolean removePermission(String name,String login,char p){

        if(name ==null||login== null){
            return false;
        }
        name = name.trim();

        FichierProtege file = findFile(name);

        if(file==null){
            return false;
        }
        if(!ControleAcces.isOwner(login, file)){
            return  false;
        }
        return file.removeP(p);

    }
}