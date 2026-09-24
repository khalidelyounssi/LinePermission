package ma.youcode.lineperm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.mindrot.jbcrypt.BCrypt;

import ma.youcode.lineperm.dao.UserDao;
import ma.youcode.lineperm.model.User;


public class UserService{

   
    // private final Path usersFile = Path.of("data", "users.txt");

        private final UserDao userDao;

        public UserService() {
            this.userDao = new UserDao();
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

        User existingUser = userDao.findByUsername(login);
        
        if (existingUser != null) {
            return false;
        }

        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        
        User user = new User(login, passwordHash);

        

        return userDao.save(user);


    }

    public User findUser(String login) {

        if (login == null) {
            return null;
        }
        login = login.trim();

        if (login.isEmpty()) {
            return null;
        }
        return userDao.findByUsername(login);
    }



    public User authenticate(String login,String password) {

    if (login == null || password == null) {
        return null;
    }

    login = login.trim();

    if (login.isEmpty() || password.isEmpty()) {
        return null;
    }

    User user = userDao.findByUsername(login);

    if (user == null) {
        return null;
    }

    boolean passwordCorrect = BCrypt.checkpw(password,user.getPasswordHash());

    if (!passwordCorrect) {
        return null;
    }

    return user;
}

//     private void loadUsers() {
//         if (!Files.exists(usersFile)) {
//             return;
//         }

//         try {
//             for (String line : Files.readAllLines(usersFile)) {
//                 String[] parts = line.split(":", 2);
//                 if (parts.length != 2) {
//                     continue;
//                 }

//                 String login = parts[0].trim();
//                 String passwordHash = parts[1].trim();
//                 if (login.isEmpty() || passwordHash.isEmpty()) {
//                     continue;
//                 }

//                 users.put(login, new User(login, passwordHash));
//             }
//         } catch (IOException e) {
//             throw new RuntimeException("Impossible de charger les utilisateurs.", e);
//         }
//     }

//     private void saveUsers() {
//         List<String> lines = new ArrayList<>();
//         for (User user : users.values()) {
//             lines.add(user.getLogin() + ":" + user.getPasswordHash());
//         }

//         try {
//             Files.createDirectories(usersFile.getParent());
//             Files.write(usersFile, lines);
//         } catch (IOException e) {
//             throw new RuntimeException("Impossible de sauvegarder les utilisateurs.", e);
//         }
//     }

}
