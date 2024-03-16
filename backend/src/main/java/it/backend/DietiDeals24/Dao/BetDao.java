package it.backend.DietiDeals24.Dao;
import it.backend.DietiDeals24.Model.Bet;

import java.util.List;

public interface BetDao<T> {

    public List<T> getBetsDAO();


}
