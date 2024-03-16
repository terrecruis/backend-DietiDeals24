package it.backend.DietiDeals24.Dao;
import java.util.List;

public interface AuctionDAO<T> {

    public List<T> getAuctionsDAO();

    public List<T> searchAuctionsDAO(String toSearch, String startPrice, String endingPrice, String category );

    public List<T> getMyAuctionsBuyerDAO(String email);

    public List<T> getMyAuctionsSellerDAO(String email);

}
