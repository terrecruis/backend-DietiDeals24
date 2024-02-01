package it.backend.DietiDeals24.DbConnection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

//using singleton pattern
public class Connessione {

    private static Connessione istanza;

    public Connection connection = null;

    private String url;
    private String nome;
    private String password;
    private String driver;

    private Connessione() throws SQLException {
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
        try (InputStream input = Connessione.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                System.out.println("Impossibile trovare il file database.properties");
                return;
            }
            prop.load(input);
            url = prop.getProperty("db.url");
            nome = prop.getProperty("db.username");
            password = prop.getProperty("db.password");
            driver = prop.getProperty("db.driver");
        } catch (IOException ex) {
            System.out.println("Errore durante la lettura del file database.properties");
            ex.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static Connessione getInstance() throws SQLException {
        if (istanza == null) {
            istanza = new Connessione();
        } else if (istanza.getConnection().isClosed()) {
            istanza = new Connessione();
        }
        return istanza;
    }
}

