package it.backend.DietiDeals24.Dao;
import java.math.BigDecimal;

public interface BetDao {

    boolean makeBetDAO(String idAuction, String emailBuyer, BigDecimal betValue);

}
