package it.backend.DietiDeals24.Dao;
import java.util.List;

public interface NotificationDAO<T> {

    public List<T> getNotificationBuyerDAO(String email);

    public List<T> getNotificationSellerDAO(String email);


}
