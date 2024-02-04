package it.backend.DietiDeals24.PostgresDao;
import it.backend.DietiDeals24.Dao.AccountDAO;
import it.backend.DietiDeals24.DbConnection.DbConnection;
import it.backend.DietiDeals24.Exception.QueryExecutionException;
import it.backend.DietiDeals24.Model.Account;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class AccountPostgresDAO implements AccountDAO<Account> {

    Connection connection;
    String query;

    public AccountPostgresDAO() {
        try {
            connection = DbConnection.getInstance().getConnection();
        } catch (SQLException e) {
            throw new QueryExecutionException("Errore durante l'apertura della connessione al database", e);
        }
    }

    @Override
    public boolean login(String email, String password) {
        query = "SELECT COUNT(*) FROM compratore WHERE email = ? AND password = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new QueryExecutionException("Errore durante l'esecuzione della query", e);
        }
        return false;
    }

    public boolean signup(String email, String password, String fullname, String telephoneNumber, String typeOfAccount) throws QueryExecutionException {

        query = setQueryforSignup(typeOfAccount);

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);
            statement.setString(3, fullname);
            statement.setString(4, telephoneNumber);
            int state = statement.executeUpdate();
            return state > 0;
        } catch (SQLException e) {
            throw new QueryExecutionException("account già esistente!", e);
        }
    }

    @Override
    public List<Account> getAllAccounts() {
        return null;
    }

    @Override
    public boolean updateAccount(Account user) {
        return false;
    }

    //ALL UTILITIES METHODS
    private String setQueryforSignup(String typeOfAccount) {
        if (typeOfAccount.equals("buyer")) {
            return "INSERT INTO compratore (email, password, nomeCompleto, number) VALUES (?, ?, ?, ?)";
        } else {
            return "INSERT INTO venditore (email, password, nomeCompleto, number) VALUES (?, ?, ?, ?)";
        }
    }

}