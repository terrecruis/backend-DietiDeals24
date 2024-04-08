package it.backend.DietiDeals24.Model;
import java.math.BigDecimal;
import java.util.List;

public class IncrementalAuction extends  Auction {

    private BigDecimal startingPrice;

    private BigDecimal raisingThreshold;

    private Integer timer;

    public IncrementalAuction(String id, Seller creator, List<Buyer> partenecipants, String title, String description, String imageAuction, String category, String location, BigDecimal startingPrice, BigDecimal raisingThreshold, Integer timer, BigDecimal currentPrice) {
        super(id, creator, partenecipants, title, description, imageAuction, category, location, currentPrice);
        this.startingPrice = startingPrice;
        this.raisingThreshold = raisingThreshold;
        this.timer = timer;
    }


    //all get
    public BigDecimal getStartingPrice() {
        return startingPrice;
    }

    public BigDecimal getRaisingThreshold() {
        return raisingThreshold;
    }

    public Integer getTimer() {
        return timer;
    }


    //all set
    public void setStartingPrice(BigDecimal startingPrice) {
        this.startingPrice = startingPrice;
    }

    public void setRaisingThreshold(BigDecimal raisingThreshold) {
        this.raisingThreshold = raisingThreshold;
    }

    public void setTimer(Integer timer) {
        this.timer = timer;
    }
}
