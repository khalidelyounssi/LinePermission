package ma.youcode.lineperm.service;

import java.util.HashMap;
import java.util.Map;
import org.mindrot.jbcrypt.BCrypt;
import ma.youcode.lineperm.model.User;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;


public class UserService{

    private final Map<String, User> users = new HashMap<>();

    private final Path usersFile =Path.of("data", "users.txt");

            public UserService() {
                readUsers();
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
        
        User user = new User(login , passwordHash);

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
        boolean passwordCorrct = BCrypt.checkpw(password,user.getPasswordHash());

        if(!passwordCorrct){
            return null;
        }
        return user;

    }



    public void saveUsers(){
        List<String> lines = new ArrayList<>();
       for(User user : users.values()){ 
        String line = user.getLogin() +":"+user.getPasswordHash();

         lines.add(line);

        

    }
            try {
            Files.createDirectories(usersFile.getParent());
            Files.write(usersFile, lines);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Impossible de sauvegarder les utilisateurs.",e);
        }
    }

    private void readUsers() {

    if (!Files.exists(usersFile)) {
        return;
    }

    try {
        List<String> lines =
                Files.readAllLines(usersFile);

        for (String line : lines) {

            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split(":", 2);

            if (parts.length != 2) {
                continue;
            }

            String login = parts[0];
            String passwordHash = parts[1];

            User user =
                    new User(login, passwordHash);

            users.put(login, user);
        }

    } catch (IOException e) {
        throw new RuntimeException(
                "impossible de charger les user",e);
    }
}
    
}