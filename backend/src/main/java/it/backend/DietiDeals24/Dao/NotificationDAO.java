package it.backend.DietiDeals24.Dao;
import java.util.List;

public interface NotificationDAO<T> {

    List<T> getNotificationBuyerDAO(String email);

    List<T> getNotificationSellerDAO(String email);


}
