package it.backend.DietiDeals24.Dao;


public interface AccountDAO<T> {

    boolean upgradePremiumAccountDAO(String email, String fullname, String telephoneNumber);

    boolean addAccountDAO(String email, String fullname, String telephoneNumber);

    T getInfoAccountDAO(String email);

    boolean updateInfoSellerAccountDAO(String email, String fullname, String telephoneNumber, String country, String description, String link1, String link2);

    boolean updateInfoBuyerAccountDAO(String email, String fullname, String telephoneNumber, String country, String description, String link1, String link2);

}