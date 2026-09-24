package ma.youcode.lineperm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ma.youcode.lineperm.model.User;

public class UserDao extends AbstractDao<User> {

    public boolean save(User user) {

        if (user == null) {
            return false;
        }

        String sql = "INSERT INTO users " + "(login, password_hash) " + "VALUES (?, ?)";

        try (
                Connection connection = getConnection();

                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getLogin());

            statement.setString(2, user.getPasswordHash());

            int lignesAjoutees = statement.executeUpdate();

            return lignesAjoutees == 1;

        } catch (SQLException e) {

            System.out.println("Erreur pendant l'ajout : " + e.getMessage());

            return false;
        }
    }

        public User findById(int id) {

                    if (id <= 0) {
                        return null;
                    }

                    String sql = "SELECT id, login, password_hash FROM users WHERE id = ?";

                    try (
                        Connection connection = getConnection();
                        PreparedStatement statement = connection.prepareStatement(sql)
                    ) {

                        statement.setInt(1,id);

                        try (ResultSet resultSet = statement.executeQuery()) {

                            if (resultSet.next()) {
                                int userId = resultSet.getInt("id");
                                String login = resultSet.getString("login");
                                String passwordHash = resultSet.getString("password_hash");

                                return new User(userId,login,passwordHash);
                            }
                        }

                    } catch (SQLException e) {
                        System.out.println("Erreur pendant la recherche : " + e.getMessage());
                    }

                    return null;
                }

    public boolean delete(int id) {

        if (id <= 0) {
            return false;
        }

        String sql = "DELETE FROM users WHERE id = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            int lignesSupprimees = statement.executeUpdate();

            return lignesSupprimees == 1;

        } catch (SQLException e) {

            System.out.println("Erreur pendant la suppression : " + e.getMessage());

            return false;
        }
    }

    public User findByUsername(String username) {

        if (username == null) {
            return null;
        }

        username = username.trim();

        if (username.isEmpty()) {
            return null;
        }

        String sql = "SELECT id, login, password_hash FROM users WHERE login = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String login = resultSet.getString("login");
                    String passwordHash = resultSet.getString("password_hash");

                    return new User(id,login,passwordHash);
                }
            }

        } catch (SQLException e) {

            System.out.println("Erreur pendant la recherche : " + e.getMessage());
        }

        return null;
    }
}