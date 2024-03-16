package it.backend.DietiDeals24.Dao;


public interface AccountDAO<T> {

    boolean addAccountDAO(String email, String fullname, String telephoneNumber);

    public T getInfoAccountDAO(String email);

}