package it.backend.DietiDeals24.PostgresDao;
import it.backend.DietiDeals24.Dao.BetDao;
import it.backend.DietiDeals24.DbConnection.DbConnection;
import it.backend.DietiDeals24.Exception.QueryExecutionException;
import it.backend.DietiDeals24.Model.Bet;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class BetPostgresDAO implements BetDao<Bet> {

    Connection connection;

    String query;


    public BetPostgresDAO() {
        try {
            connection = DbConnection.getInstance().getConnection();
        } catch (SQLException e) {
            throw new QueryExecutionException("Errore durante l'apertura della connessione al database", e);
        }
    }


    @Override
    public boolean makeBetDAO(String idAuction, String emailBuyer, BigDecimal betValue) {
        query = "INSERT INTO puntata (idasta, emailcompratore, importo) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, idAuction);
            statement.setString(2, emailBuyer);
            statement.setBigDecimal(3, betValue);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
