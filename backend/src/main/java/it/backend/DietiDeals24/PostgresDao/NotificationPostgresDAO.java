package it.backend.DietiDeals24.PostgresDao;
import it.backend.DietiDeals24.Dao.NotificationDAO;
import it.backend.DietiDeals24.DbConnection.DbConnection;
import it.backend.DietiDeals24.Exception.QueryExecutionException;
import it.backend.DietiDeals24.Model.Buyer;
import it.backend.DietiDeals24.Model.Notification;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationPostgresDAO implements NotificationDAO<Notification> {

    private final Connection connection;

    public NotificationPostgresDAO() {
        try {
            connection = DbConnection.getInstance().getConnection();
        } catch (SQLException e) {
            throw new QueryExecutionException("Errore durante l'apertura della connessione al database", e);
        }
    }

    private List<Notification> getNotificationsByQuery(String query, String email) {
        List<Notification> notifications = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String title = resultSet.getString("titoloAsta");
                    String status = resultSet.getString("descrizione");
                    Date time = resultSet.getDate("tempo");
                    String image = resultSet.getString("immagine");

                    Notification notification = new Notification(new Buyer(email), image, status, title, time);
                    notifications.add(notification);
                }
            }
        } catch (SQLException e) {
            throw new QueryExecutionException("Errore durante il recupero delle notifiche dal database", e);
        }

        return notifications;
    }

    @Override
    public List<Notification> getNotificationBuyerDAO(String email) {
        String query = "SELECT titoloAsta, descrizione, tempo, immagine " +
                "FROM NOTIFICA " +
                "WHERE emailCompratore = ? " +
                "ORDER BY tempo DESC";

        return getNotificationsByQuery(query, email);
    }

    @Override
    public List<Notification> getNotificationSellerDAO(String email) {
        String query = "SELECT titoloAsta, descrizione, tempo, immagine " +
                "FROM NOTIFICA " +
                "WHERE emailVenditore = ? AND emailCompratore IS NULL " +
                "ORDER BY tempo DESC";

        return getNotificationsByQuery(query, email);
    }

}
