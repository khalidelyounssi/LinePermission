package ma.youcode.lineperm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

import ma.youcode.lineperm.access.ControleAcces;
import ma.youcode.lineperm.model.FichierProtege;

public class FileService {

    private final Map<String, FichierProtege> fichiers = new HashMap<>();

    private final Path filesDirectory =Path.of("data", "files");

    private final Path permissionsFile =Path.of("data", "files.txt");

    private  final LogAnalyzerService logAnalyzerService;

    public FileService(LogAnalyzerService logAnalyzerService) {

    try {
        Files.createDirectories(filesDirectory);
        this.logAnalyzerService=logAnalyzerService;
    } catch (IOException e) {
        throw new RuntimeException(
                "Impossible de creer le dossier des fichiers.",
                e
        );
    }
    loadFiles();
    
}


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

    Path filePath = filesDirectory.resolve(name);

    if (Files.exists(filePath)) {
        return false;
    }

    FichierProtege file =
            new FichierProtege(name, owner);

    try {

        Files.createFile(filePath);

        fichiers.put(name, file);

        saveFiles();

        return true;

    } catch (IOException e) {

        return false;
    }
}


    public List<String> listFiles() {

    List<String> lines = new ArrayList<>();

    for (FichierProtege file : fichiers.values()) {

        String line =file.getPermissions() + " " +file.getOwner() + " " +file.getNom();

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
            Path filePath = filesDirectory.resolve(name);

                    try {

                        Files.writeString(filePath, contenu);

                        return true;

                    } catch (IOException e) {

                        return false;
                    }
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

        Path filePath = filesDirectory.resolve(name);
       try {
        if (!ControleAcces.canAccess(login, file, 'r')) {
            logAnalyzerService.seveLog(login,"LECTURE",file.getNom(),"REFUSE");

            return null;
        }

        String contenu = Files.readString(filePath);

        logAnalyzerService.seveLog(login, "LECTURE",file.getNom(),"OK");

        return contenu;

    } catch (IOException e) {
        return null;
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
        boolean success = file.grantP(p);

            if (success) {
                saveFiles();
            }

            return success;

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
        boolean success = file.removeP(p);

            if (success) {
                saveFiles();
            }

            return success;

    }


    private void saveFiles() {

    List<String> lines = new ArrayList<>();

    for (FichierProtege file : fichiers.values()) {

        String permissions = file.getPermissions();

        String ownerPermissions = permissions.substring(0, 3);
        String autresPermissions = permissions.substring(4);

        String line =file.getNom() + ";" +file.getOwner() + ";" +ownerPermissions + ";" +autresPermissions;

        lines.add(line);
    }

    try {
        Files.createDirectories(permissionsFile.getParent());
        Files.write(permissionsFile, lines);
    } catch (IOException e) {
        throw new RuntimeException(
                "Impossible de sauvegarder les fichiers.",
                e
        );
    }
}


        private void loadFiles() {

    if (!Files.exists(permissionsFile)) {
        return;
    }

    try {
        List<String> lines = Files.readAllLines(permissionsFile);

        for (String line : lines) {

            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split(";", 4);

            if (parts.length != 4) {
                continue;
            }

            String name = parts[0];
            String owner = parts[1];
            String ownerPermissions = parts[2];
            String autresPermissions = parts[3];

            if (!isValidName(name)) {
                continue;
            }

            if (ownerPermissions.length() != 3
                    || autresPermissions.length() != 3) {
                continue;
            }

            Path filePath = filesDirectory.resolve(name);

            if (!Files.exists(filePath)) {
                continue;
            }

            FichierProtege file = new FichierProtege(
                    name,
                    owner,
                    ownerPermissions.charAt(0) == 'r',
                    ownerPermissions.charAt(1) == 'w',
                    ownerPermissions.charAt(2) == 'd',
                    autresPermissions.charAt(0) == 'r',
                    autresPermissions.charAt(1) == 'w',
                    autresPermissions.charAt(2) == 'd'
            );

            fichiers.put(name, file);
        }

    } catch (IOException e) {
        throw new RuntimeException(
                "Impossible de charger les fichiers.",
                e
        );
    }
}
}
