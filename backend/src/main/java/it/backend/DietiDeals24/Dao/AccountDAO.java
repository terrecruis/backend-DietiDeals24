package it.backend.DietiDeals24.Dao;


public interface AccountDAO<T> {


    boolean upgradePremiumAccountDAO(String email, String fullname, String telephoneNumber);

    boolean addAccountDAO(String email, String fullname, String telephoneNumber);

    boolean updateToSellerMode(String email);

    public T getInfoAccountDAO(String email);

}