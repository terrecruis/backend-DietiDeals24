package it.backend.DietiDeals24.Dao;
import it.backend.DietiDeals24.Model.FixedTimeAuction;
import it.backend.DietiDeals24.Model.IncrementalAuction;

import java.util.List;

public interface AuctionDAO<T> {


    void updateStatusAuctionsDAO();

    List<T> getAuctionsDAO();

    List<T> searchAuctionsDAO(String toSearch, String startPrice, String endingPrice, String category );

    List<T> getMyAuctionsBuyerDAO(String email);

    List<T> getMyAuctionsSellerDAO(String email);

    boolean insertIncrementalAuctionDAO(IncrementalAuction auction);

    boolean insertFixedTimeAuctionDAO(FixedTimeAuction auction);

    String getNextAuctionId();

}
