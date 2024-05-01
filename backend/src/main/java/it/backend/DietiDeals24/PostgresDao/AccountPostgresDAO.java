package it.backend.DietiDeals24.PostgresDao;
import it.backend.DietiDeals24.Dao.AccountDAO;
import it.backend.DietiDeals24.DbConnection.DbConnection;
import it.backend.DietiDeals24.Exception.QueryExecutionException;
import it.backend.DietiDeals24.Model.Account;
import it.backend.DietiDeals24.Model.Buyer;
import it.backend.DietiDeals24.Model.Seller;
import it.backend.DietiDeals24.Model.SocialLink;
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
        if (userExistsWithEmail(email)) {
            return true;
        }

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

    private boolean userExistsWithEmail(String email) {
        query = "SELECT COUNT(*) FROM compratore WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            throw new QueryExecutionException("Errore durante la verifica dell'esistenza dell'utente nel database", e);
        }
        return false;
    }


    @Override
    public boolean upgradePremiumAccountDAO(String email) {
        String selectQuery = "SELECT email FROM venditore WHERE email = ?";
        try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
            selectStatement.setString(1, email);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true; // Esiste già un venditore con questa email
                } else {
                    // Ottieni i dati dal compratore
                    String buyerDataQuery = "SELECT nomeCompleto, telephoneNumber FROM compratore WHERE email = ?";
                    try (PreparedStatement buyerDataStatement = connection.prepareStatement(buyerDataQuery)) {
                        buyerDataStatement.setString(1, email);
                        try (ResultSet buyerDataResultSet = buyerDataStatement.executeQuery()) {
                            if (buyerDataResultSet.next()) {
                                // Inserisci i dati del compratore nella tabella venditore
                                String insertQuery = "INSERT INTO venditore (email, nomeCompleto, telephoneNumber) VALUES (?, ?, ?)";
                                try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                                    insertStatement.setString(1, email);
                                    insertStatement.setString(2, buyerDataResultSet.getString("nomeCompleto"));
                                    insertStatement.setString(3, buyerDataResultSet.getString("telephoneNumber"));
                                    int rowsAffected = insertStatement.executeUpdate();
                                    return rowsAffected > 0;
                                }
                            } else {
                                // Il compratore non esiste
                                return false;
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new QueryExecutionException("Errore durante l'aggiornamento dell'account a premium nel database", e);
        }
    }



    @Override
    public boolean updateInfoSellerAccountDAO(String email, String fullName, String telephoneNumber, String country, String description, String link1, String link2) {
        query = "UPDATE venditore SET nomeCompleto = ?, telephoneNumber = ?, nazionalita = ?, descrizione = ?, link1 = ?, link2 = ? WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            setPreparedStatementParams(statement, fullName, telephoneNumber, country, description, link1, link2, email);
            int state = statement.executeUpdate();
            return state > 0;
        } catch (SQLException e) {
            throw new QueryExecutionException("problema aggiornamento utente nel database!", e);
        }
    }

    @Override
    public boolean updateInfoBuyerAccountDAO(String email, String fullName, String telephoneNumber, String country, String description, String link1, String link2) {
        query = "UPDATE compratore SET nomeCompleto = ?, telephoneNumber = ?, nazionalita = ?, descrizione = ?, link1 = ?, link2 = ? WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            setPreparedStatementParams(statement, fullName, telephoneNumber, country, description, link1, link2, email);
            int state = statement.executeUpdate();
            return state > 0;
        } catch (SQLException e) {
            throw new QueryExecutionException("problema aggiornamento utente nel database!", e);
        }
    }

    @Override
    public Account getInfoSellerAccountByEmailDAO(String email) {
        return getInfoAccountByEmailDAO(email, "venditore");
    }

    @Override
    public Account getInfoBuyerAccountByEmailDAO(String email) {
        return getInfoAccountByEmailDAO(email, "compratore");
    }

    public Account getInfoAccountByEmailDAO(String email, String tableName) {
        query = "SELECT * FROM " + tableName + " WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String emailAccount = resultSet.getString("email");
                    String fullName = resultSet.getString("nomeCompleto");
                    String foto = resultSet.getString("foto");
                    String telephoneNumber = resultSet.getString("telephoneNumber");
                    String description = resultSet.getString("descrizione");
                    String country = resultSet.getString("nazionalita");
                    String link1 = resultSet.getString("link1");
                    String link2 = resultSet.getString("link2");
                    System.out.println("email: " + emailAccount);

                    Account account;
                    if (tableName.equals("venditore")) {
                        account = new Seller(fullName, foto, emailAccount, description, telephoneNumber, country);
                    } else {
                        account = new Buyer(fullName, foto, emailAccount, description, telephoneNumber, country);
                    }
                    account.addSocialLink(new SocialLink(link1));
                    account.addSocialLink(new SocialLink(link2));
                    return account;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new QueryExecutionException("Problema durante il recupero delle informazioni dell'account dal database!", e);
        }
    }




    private void setPreparedStatementParams(PreparedStatement statement, String fullName, String telephoneNumber, String country, String description, String link1, String link2, String email) throws SQLException {
        statement.setString(1, fullName);
        statement.setString(2, telephoneNumber);
        statement.setString(3, country);
        statement.setString(4, description);
        statement.setString(5, link1);
        statement.setString(6, link2);
        statement.setString(7, email);
    }
}