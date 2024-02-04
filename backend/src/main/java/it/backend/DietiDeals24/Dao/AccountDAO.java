package it.backend.DietiDeals24.Dao;
import java.util.List;

public interface AccountDAO<T> {

    boolean login(String email, String password);

    boolean signup(String email, String password, String fullname, String telephoneNumber, String typeOfAccount);

    List<T> getAllAccounts();

    boolean updateAccount(T user);

}
