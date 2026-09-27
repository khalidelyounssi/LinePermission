package ma.youcode.lineperm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ma.youcode.lineperm.model.AccessLog;

public class LogDao extends AbstractDao<AccessLog> {

    public boolean save(AccessLog log) {

        if (log == null) {
            return false;
        }

        String sql = "INSERT INTO logs (user_id, fichier_id, action, resultat, date_heure) "+ "VALUES ((SELECT id FROM users WHERE login = ?), "+ "(SELECT id FROM fichiers WHERE nom = ?), ?, ?, ?)";

        try {
            Connection connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, log.getUtilisateur());
                statement.setString(2, log.getFichier());
                statement.setString(3, log.getAction());
                statement.setString(4, log.getResultat());
                statement.setString(5, log.getDate() + " " + log.getHeure());

                return statement.executeUpdate() == 1;
            }

        } catch (SQLException e) {
            System.out.println("Erreur pendant l'ajout du log : " + e.getMessage());
            return false;
        }
    }

    public AccessLog findById(int id) {

        if (id <= 0) {
            return null;
        }

        String sql = baseSelect() + " WHERE l.id = ?";

        try {
            Connection connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return createLog(resultSet);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur pendant la recherche du log : " + e.getMessage());
        }

        return null;
    }

    public boolean delete(int id) {
        return false;
    }

    public long compterTotal() {
        return compter("SELECT COUNT(*) AS total FROM logs");
    }

    public long compterRefuses() {
        return compter("SELECT COUNT(*) AS total FROM logs WHERE resultat = 'REFUSE'");
    }

    public List<String> userDistincts() {

        List<String> users = new ArrayList<>();
        String sql = "SELECT DISTINCT u.login FROM logs l "+ "JOIN users u ON u.id = l.user_id ORDER BY u.login";

        try {
            Connection connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    users.add(resultSet.getString("login"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur pendant la recherche des utilisateurs : " + e.getMessage());
        }

        return users;
    }

    public Map<String, Long> actionsByUser() {

        Map<String, Long> actions = new LinkedHashMap<>();
        String sql = "SELECT u.login, COUNT(*) AS total FROM logs l "+ "JOIN users u ON u.id = l.user_id "+ "GROUP BY u.id, u.login ORDER BY total DESC";

        try {
            Connection connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    actions.put(resultSet.getString("login"), resultSet.getLong("total"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur pendant le calcul des actions : " + e.getMessage());
        }

        return actions;
    }

    public List<Map.Entry<String, Long>> topFichiers(int limite) {

        List<Map.Entry<String, Long>> fichiers = new ArrayList<>();

        if (limite <= 0) {
            return fichiers;
        }

        String sql = "SELECT f.nom, COUNT(*) AS total FROM logs l "+ "JOIN fichiers f ON f.id = l.fichier_id "+ "GROUP BY f.id, f.nom ORDER BY total DESC LIMIT ?";

        try {
            Connection connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, limite);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        fichiers.add(new AbstractMap.SimpleEntry<>(
                                resultSet.getString("nom"),
                                resultSet.getLong("total")));
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur pendant le calcul du top fichiers : " + e.getMessage());
        }

        return fichiers;
    }

    public List<AccessLog> refusesByUser(String username) {

        List<AccessLog> logs = new ArrayList<>();

        if (username == null || username.trim().isEmpty()) {
            return logs;
        }

        String sql = baseSelect()+ " WHERE u.login = ? AND l.resultat = 'REFUSE' ORDER BY l.date_heure DESC";

        try {
            Connection connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username.trim());

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        logs.add(createLog(resultSet));
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur pendant la recherche des refus : " + e.getMessage());
        }

        return logs;
    }

    public Optional<String> userPlusActif() {

        String sql = "SELECT u.login, COUNT(*) AS total FROM logs l "+ "JOIN users u ON u.id = l.user_id "+ "GROUP BY u.id, u.login ORDER BY total DESC LIMIT 1";

        try {
            Connection connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return Optional.of(resultSet.getString("login"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur pendant la recherche de l'utilisateur actif : " + e.getMessage());
        }

        return Optional.empty();
    }

    public Map<String, Long> repartitionByAction() {

        Map<String, Long> actions = new LinkedHashMap<>();
        String sql = "SELECT action, COUNT(*) AS total FROM logs "+ "GROUP BY action ORDER BY action";

        try {
            Connection connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    actions.put(resultSet.getString("action"), resultSet.getLong("total"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur pendant le calcul des actions : " + e.getMessage());
        }

        return actions;
    }

    private long compter(String sql) {

        try {
            Connection connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getLong("total");
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur pendant le comptage : " + e.getMessage());
        }

        return 0;
    }

    private String baseSelect() {
        return "SELECT l.id, date(l.date_heure) AS date, "+ "strftime('%H:%M', l.date_heure) AS heure, "+ "u.login AS utilisateur, l.action, f.nom AS fichier, l.resultat "+ "FROM logs l "
                + "JOIN users u ON u.id = l.user_id "
                + "JOIN fichiers f ON f.id = l.fichier_id";
    }

    private AccessLog createLog(ResultSet resultSet) throws SQLException {
        return new AccessLog(
                resultSet.getInt("id"),
                resultSet.getString("date"),
                resultSet.getString("heure"),
                resultSet.getString("utilisateur"),
                resultSet.getString("action"),
                resultSet.getString("fichier"),
                resultSet.getString("resultat"));
    }
}
