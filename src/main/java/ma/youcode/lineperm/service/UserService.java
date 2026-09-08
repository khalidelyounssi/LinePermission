package ma.youcode.lineperm.service;

import java.util.HashMap;
import java.util.Map;
import org.mindrot.jbcrypt.BCrypt;
import ma.youcode.lineperm.model.User;


public class UserService{

    private final Map<String, User> users = new HashMap<>();
   


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
        
        User user = new User(login , password);

        users.put(login , user);

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
    
}