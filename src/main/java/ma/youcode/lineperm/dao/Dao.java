package ma.youcode.lineperm.dao;

public interface Dao<T> {

    boolean save(T objet);

    
    T findById(int id);



    boolean delete(int id);
}