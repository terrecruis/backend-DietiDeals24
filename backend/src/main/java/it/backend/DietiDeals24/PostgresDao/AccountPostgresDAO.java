package it.backend.DietiDeals24.PostgresDao;
import it.backend.DietiDeals24.Dao.AccountDAO;
import it.backend.DietiDeals24.DbConnection.DbConnection;
import it.backend.DietiDeals24.Exception.QueryExecutionException;
import it.backend.DietiDeals24.Model.Account;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
            throw new QueryExecutionException("problema aggiunta utente nel database!", e);
        }
    }

    @Override
    public boolean updateToSellerMode(String email) {
        query = "SELECT email, nomeCompleto, telephoneNumber FROM compratore WHERE email = ?";

        //esegui la query e inserisci i risulati all'interno della tabaella venditore
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            int state = statement.executeUpdate();
            return state > 0;
        } catch (SQLException e) {
            throw new QueryExecutionException("problema aggiunta utente nel database!", e);
        }
    }


    @Override
    public boolean upgradePremiumAccountDAO(String email, String fullName, String telephoneNumber) {
        // Verifica prima se l'account è già presente nella tabella venditore
        String selectQuery = "SELECT email FROM venditore WHERE email = ?";
        try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
            selectStatement.setString(1, email);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    // L'account è già presente nella tabella venditore, quindi non è necessario fare nulla
                    return true; // Indica che l'account è già aggiornato a premium
                } else {
                    // L'account non è presente nella tabella venditore, quindi lo aggiungo
                    String insertQuery = "INSERT INTO venditore (email, nomeCompleto, telephoneNumber) VALUES (?, ?, ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                        insertStatement.setString(1, email);
                        insertStatement.setString(2, fullName);
                        insertStatement.setString(3, telephoneNumber);
                        int rowsAffected = insertStatement.executeUpdate();
                        return rowsAffected > 0; // Ritorna true se almeno una riga è stata aggiunta
                    }
                }
            }
        } catch (SQLException e) {
            throw new QueryExecutionException("Errore durante l'aggiornamento dell'account a premium nel database", e);
        }
    }




    @Override
    public Account getInfoAccountDAO(String email) {
        return null;
    }
}