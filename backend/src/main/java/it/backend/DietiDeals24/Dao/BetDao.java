package it.backend.DietiDeals24.Dao;
import it.backend.DietiDeals24.Model.Bet;

import java.math.BigDecimal;
import java.util.List;

public interface BetDao<T> {

    boolean makeBetDAO(String idAuction, String emailBuyer, BigDecimal betValue);

}
