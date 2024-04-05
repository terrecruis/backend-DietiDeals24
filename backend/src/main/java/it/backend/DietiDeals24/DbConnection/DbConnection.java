package it.backend.DietiDeals24.DbConnection;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnection {

    private static DbConnection instance;

    private Connection connection = null;

    private String url;
    private String nome;
    private String password;
    private String driver;

    private DbConnection() {
        loadDatabaseProprieties();
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, nome, password);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Database Connection Creation Failed : " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void loadDatabaseProprieties() {
        Properties prop = new Properties();
        try (InputStream input = Files.newInputStream(Paths.get("configuration.properties"))) {
            prop.load(input);
            url = prop.getProperty("db.url");
            nome = prop.getProperty("db.username");
            password = prop.getProperty("db.password");
            driver = prop.getProperty("db.driver");
        } catch (IOException ex) {
            System.out.println("Errore durante la lettura del file configuration.properties");
            ex.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static DbConnection getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new DbConnection();
        }
        return instance;
    }
}

