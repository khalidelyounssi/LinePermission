package ma.youcode.lineperm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.mindrot.jbcrypt.BCrypt;
import ma.youcode.lineperm.model.User;


public class UserService{

    private final Map<String, User> users = new HashMap<>();
    private final Path usersFile = Path.of("data", "users.txt");

    public UserService() {
        loadUsers();
    }
   


    public boolean createUser(String login ,  String password){

        if (login == null|| password==null){
            return false;
        }

        login = login.trim();

        if (login.isEmpty()||password.isEmpty()){
            return false;
        }
        if (login.contains(" ")||login.contains(":")){
            return false;
        }

        if (users.containsKey(login)){
            return false;
        }

        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        
        User user = new User(login, passwordHash);

        users.put(login , user);
        saveUsers();

        return true;


    }

    public User findUser(String login){
        if(login == null){
            return null;
        }
        return users.get(login);
    }






    public User authenticate(String login ,String password){
        if(login==null||password==null){
            return null;


        }

        login=login.trim();

        User user=users.get(login);

        if(user==null){
            return null;
        }
        boolean passwordCorrct;
        try {
            passwordCorrct = BCrypt.checkpw(password, user.getPasswordHash());
        } catch (IllegalArgumentException e) {
            return null;
        }

        if(!passwordCorrct){
            return null;
        }
        return user;

    }

    private void loadUsers() {
        if (!Files.exists(usersFile)) {
            return;
        }

        try {
            for (String line : Files.readAllLines(usersFile)) {
                String[] parts = line.split(":", 2);
                if (parts.length != 2) {
                    continue;
                }

                String login = parts[0].trim();
                String passwordHash = parts[1].trim();
                if (login.isEmpty() || passwordHash.isEmpty()) {
                    continue;
                }

                users.put(login, new User(login, passwordHash));
            }
        } catch (IOException e) {
            throw new RuntimeException("Impossible de charger les utilisateurs.", e);
        }
    }

    private void saveUsers() {
        List<String> lines = new ArrayList<>();
        for (User user : users.values()) {
            lines.add(user.getLogin() + ":" + user.getPasswordHash());
        }

        try {
            Files.createDirectories(usersFile.getParent());
            Files.write(usersFile, lines);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de sauvegarder les utilisateurs.", e);
        }
    }
    
}
