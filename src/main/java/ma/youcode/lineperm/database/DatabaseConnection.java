package ma.youcode.lineperm.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:data/audit.db";

    private static final DatabaseConnection INSTANCE =new DatabaseConnection();

    private Connection connection;

    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(URL);
            enableForeignKeys();
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de se connecter a la base de donnees.", e);
        }
    }

    public static DatabaseConnection getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL);
            enableForeignKeys();
        }

        return connection;
    }

    private void enableForeignKeys() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }
    }
}
