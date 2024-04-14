package it.backend.DietiDeals24.Service;
import it.backend.DietiDeals24.Dao.AuctionDAO;
import it.backend.DietiDeals24.Model.Auction;
import it.backend.DietiDeals24.PostgresDao.AuctionPostgresDAO;
import java.util.List;


public class AuctionService {

    final AuctionDAO<Auction> auction = new AuctionPostgresDAO();

    public AuctionService() {
        auction.updateStatusAuctionsDAO();
    }

    public List<Auction> getAuctionsService() {
        return auction.getAuctionsDAO();
    }

    public List<Auction> getMyAuctionsBuyerService(String email) {
        return auction.getMyAuctionsBuyerDAO(email);
    }


    public List<Auction> getMyAuctionsSellerService(String email) {
        return auction.getMyAuctionsSellerDAO(email);
    }

    public List<Auction> getAuctionsByCategoryAndPriceService(String toSearch, String startingPrice, String endingPrice, String category) {
        return auction.searchAuctionsDAO(toSearch, startingPrice, endingPrice, category);
    }

}