package ma.youcode.lineperm.service;

import java.util.HashMap;
import java.util.Map;

import ma.youcode.lineperm.model.User;


public class UserService{

    private final Map<String, User> users = new HashMap<>();
   


    public boolean createUser(String login ,  String passwordHash){

        if (login == null|| passwordHash==null){
            return false;
        }

        login = login.trim();

        if (login.isEmpty()||passwordHash.isEmpty()){
            return false;
        }
        if (login.contains(" ")||login.contains(":")){
            return false;
        }

        if (users.containsKey(login)){
            return false;
        }
        
        User user = new User(login , passwordHash);

        users.put(login , user);

        return true;


    }

    public User findUser(String login){
        if(login == null){
            return null;
        }
        return users.get(login);
    }
}