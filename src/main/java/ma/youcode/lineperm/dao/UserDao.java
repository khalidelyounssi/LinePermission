package ma.youcode.lineperm.dao;

import ma.youcode.lineperm.model.User;

public class UserDao extends AbstractDao<User> {

   
    public boolean save(User user){
       
        return false;
    }

    
    public User findById(int id){

        return null;
    }

   
    public boolean delete(int id){

        return false;
    }

    public User findByUsername(String username){

        return null;
    }
}