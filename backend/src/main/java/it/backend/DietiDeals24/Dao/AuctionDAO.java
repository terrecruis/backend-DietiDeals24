package it.backend.DietiDeals24.Dao;
import java.util.List;

public interface AuctionDAO<T> {


    void updateStatusAuctionsDAO();

    List<T> getAuctionsDAO();

    List<T> searchAuctionsDAO(String toSearch, String startPrice, String endingPrice, String category );

    List<T> getMyAuctionsBuyerDAO(String email);

    List<T> getMyAuctionsSellerDAO(String email);

}
