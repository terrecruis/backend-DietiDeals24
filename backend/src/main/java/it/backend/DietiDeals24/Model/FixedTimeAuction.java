package it.backend.DietiDeals24.Model;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class FixedTimeAuction extends Auction {

    private Date endOfAuction;

    private BigDecimal minimumSecretThreshold;


    public FixedTimeAuction(String id, Seller creator, List<Buyer> partenecipants, String title, String description, String imageAuction, String category, String location, Date endOfAuction, BigDecimal minimumSecretThreshold, BigDecimal currentPrice) {
        super(id, creator, partenecipants, title, description, imageAuction, category, location, currentPrice);
        this.endOfAuction = endOfAuction;
        this.minimumSecretThreshold = minimumSecretThreshold;
    }
    //all get
    public Date getEndOfAuction() {
        return endOfAuction;
    }

    public BigDecimal getMinimumSecretThreshold() {
        return minimumSecretThreshold;
    }

    //all set
    public void setEndOfAuction(Date endOfAuction) {
        this.endOfAuction = endOfAuction;
    }

    public void setMinimumSecretThreshold(BigDecimal minimumSecretThreshold) {
        this.minimumSecretThreshold = minimumSecretThreshold;
    }
}