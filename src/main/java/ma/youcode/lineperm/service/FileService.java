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

import ma.youcode.lineperm.dao.FichierDao;

public class FileService {

    private final Map<String, FichierProtege> fichiers = new HashMap<>();

    private final Path filesDirectory = Path.of("data", "files");

    private final Path permissionsFile = Path.of("data", "files.txt");

    private final FichierDao fichierDao = new FichierDao();

    public FileService() {

        try {
            Files.createDirectories(filesDirectory);
        } catch (IOException e) {
            throw new RuntimeException(
                    "impossible de creer le dossier des fichiers.",
                    e);
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

        if (name.contains("/") || name.contains("..") || name.contains("\\")) {
            return false;
        }

        return true;
    }



    public boolean createFile(String name, String owner) {

        if (!isValidName(name) || owner == null) {
            return false;
        }

        name = name.trim();
        owner = owner.trim();

        if (owner.isEmpty()) {
            return false;
        }

        if (fichierDao.findByName(name) != null) {
            return false;
        }

        Path physicalFile = filesDirectory.resolve(name);

        if (Files.exists(physicalFile)) {
            return false;
        }

        FichierProtege fichier = new FichierProtege(name, owner);

        boolean savedInDatabase = fichierDao.save(fichier);

        if (!savedInDatabase) {
            return false;
        }

        try {
            Files.createDirectories(filesDirectory);
            Files.createFile(physicalFile);
            return true;

        } catch (IOException e) {
            fichierDao.deleteByName(name);
            System.out.println("erreur pendant la creation du fichier : " + e.getMessage());
            return false;
        }
    }




    public List<String> listFiles() {

        List<String> lines = new ArrayList<>();

        List<FichierProtege> files = fichierDao.findAll();

        for (FichierProtege file : files) {
            String line = file.getPermissions() + " " + file.getOwner() + " " + file.getNom();
            lines.add(line);
        }

        return lines;
    }




    public FichierProtege findFile(String name) {
        if (name == null) {
            return null;
        }
        name = name.trim();
        if (name.isEmpty()) {
            return null;
        }
        return fichierDao.findByName(name);

    }




    public boolean canReadFile(String name, String login) {
        if (name == null || login == null) {
            return false;
        }
        name = name.trim();

        FichierProtege file = findFile(name);

        if (file == null) {
            return false;
        }
        if (!ControleAcces.canAccess(login, file, 'r')) {
            return false;
        }
        return true;

    }



    public boolean canWriteFile(String name, String login) {

        if (name == null || login == null) {
            return false;
        }

        FichierProtege file = findFile(name);

        if (file == null) {
            return false;
        }

        return ControleAcces.canAccess(login, file, 'w');
    }




    public boolean writeFile(String name, String login, String contenu) {

        if (name == null || login == null || contenu == null) {
            return false;
        }

        name = name.trim();
        login = login.trim();

        if (name.isEmpty() || login.isEmpty()) {
            return false;
        }

        FichierProtege file = findFile(name);

        if (file == null) {
            return false;
        }

        if (!ControleAcces.canAccess(login, file, 'w')) {
            return false;
        }

        Path physicalFile = filesDirectory.resolve(name);

        try {
            Files.createDirectories(filesDirectory);
            Files.writeString(physicalFile, contenu);
            return true;

        } catch (IOException e) {
            System.out.println("erreur pendant ecriture du fichier : " + e.getMessage());
            return false;
        }
    }




    public String readFile(String name, String login) {

        if (name == null || login == null) {
            return null;
        }

        name = name.trim();
        login = login.trim();

        if (name.isEmpty() || login.isEmpty()) {
            return null;
        }

        FichierProtege file = findFile(name);

        if (file == null) {
            return null;
        }

        if (!ControleAcces.canAccess(login, file, 'r')) {
            return null;
        }

        Path physicalFile = filesDirectory.resolve(name);

        if (!Files.exists(physicalFile)) {
            return null;
        }

        try {
            return Files.readString(physicalFile);

        } catch (IOException e) {
            System.out.println("erreur pendant la lecture du fichier : " + e.getMessage());
            return null;
        }
    }




    public boolean grantPermission(String name, String login, char p) {

        if (name == null || login == null) {
            return false;
        }

        name = name.trim();
        login = login.trim();

        if (name.isEmpty() || login.isEmpty()) {
            return false;
        }

        FichierProtege file = findFile(name);

        if (file == null) {
            return false;
        }

        if (!ControleAcces.isOwner(login, file)) {
            return false;
        }

        boolean permissionChanged = file.grantP(p);

        if (!permissionChanged) {
            return false;
        }

        return fichierDao.updateDroitsByName(name, file.getPermissions());
    }



    public boolean removePermission(String name, String login, char p) {

        if (name == null || login == null) {
            return false;
        }

        name = name.trim();
        login = login.trim();

        if (name.isEmpty() || login.isEmpty()) {
            return false;
        }

        FichierProtege file = findFile(name);

        if (file == null) {
            return false;
        }

        if (!ControleAcces.isOwner(login, file)) {
            return false;
        }

        boolean permissionChanged = file.removeP(p);

        if (!permissionChanged) {
            return false;
        }

        return fichierDao.updateDroitsByName(name, file.getPermissions());
    }



    private void saveFiles() {

        List<String> lines = new ArrayList<>();

        for (FichierProtege file : fichiers.values()) {

            String permissions = file.getPermissions();

            String ownerPermissions = permissions.substring(0, 3);
            String autresPermissions = permissions.substring(4);

            String line = file.getNom() + ";" + file.getOwner() + ";" + ownerPermissions + ";" + autresPermissions;

            lines.add(line);
        }

        try {
            Files.createDirectories(permissionsFile.getParent());
            Files.write(permissionsFile, lines);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Impossible de sauvegarder les fichiers.",
                    e);
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
                        autresPermissions.charAt(2) == 'd');

                fichiers.put(name, file);
            }

        } catch (IOException e) {
            throw new RuntimeException(
                    "impossible de charger les fichiers.",
                    e);
        }
    }
}
