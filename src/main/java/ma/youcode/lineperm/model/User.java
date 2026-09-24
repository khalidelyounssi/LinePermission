package ma.youcode.lineperm.model;



public class User{

    private final int id;
    private final String login;
    private final String passwordHash;

        public User(int id ,String login , String passwordHash){
            this.id=id;
            this.login=login;
            this.passwordHash = passwordHash;

        }
        public User(String login , String passwordHash){
            this.id=0;
            this.login=login;
            this.passwordHash = passwordHash;

        }
      
        public String getLogin(){
            return login;

        }
        public int getId(){
            return id;

        }
        public String getPasswordHash(){
            return passwordHash;
        }
}