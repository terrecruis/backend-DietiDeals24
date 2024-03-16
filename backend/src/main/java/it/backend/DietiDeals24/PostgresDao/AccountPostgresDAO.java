package it.backend.DietiDeals24.PostgresDao;
import it.backend.DietiDeals24.Dao.AccountDAO;
import it.backend.DietiDeals24.DbConnection.DbConnection;
import it.backend.DietiDeals24.Exception.QueryExecutionException;
import it.backend.DietiDeals24.Model.Account;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


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
    public boolean addAccountDAO(String email, String fullname, String telephoneNumber) {
        query = "INSERT INTO compratore (email, nomeCompleto, telephoneNumber) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, fullname);
            statement.setString(3, telephoneNumber);
            int state = statement.executeUpdate();
            return state > 0;
        } catch (SQLException e) {
            throw new QueryExecutionException("problema aggiunta nel database!", e);
        }
    }

    @Override
    public Account getInfoAccountDAO(String email) {
        return null;
    }
}