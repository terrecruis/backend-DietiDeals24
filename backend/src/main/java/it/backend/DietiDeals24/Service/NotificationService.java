package it.backend.DietiDeals24.Service;
import it.backend.DietiDeals24.Dao.NotificationDAO;
import it.backend.DietiDeals24.Model.Notification;
import it.backend.DietiDeals24.PostgresDao.NotificationPostgresDAO;

import java.util.List;

public class NotificationService {

    NotificationDAO<Notification> notification = new NotificationPostgresDAO();


    public List<Notification> getNotificationBuyerService(String email) {
        return notification.getNotificationBuyerDAO(email);
    }

    public List<Notification> getNotificationSellerService(String email) {
        return notification.getNotificationSellerDAO(email);
    }

}
