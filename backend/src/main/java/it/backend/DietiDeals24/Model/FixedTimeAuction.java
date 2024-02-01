package it.backend.DietiDeals24.Model;

import java.util.ArrayList;
import java.util.Date;

public class FixedTimeAuction extends Auction {

    private Date endOfAuction;

    private Integer minimumSecretThreshold;


    public FixedTimeAuction(Seller creator, ArrayList<Buyer> partenecipants, String title, String description, String imageAuction, String category, String location, Date endOfAuction, Integer minimumSecretThreshold) {
        super(creator, partenecipants, title, description, imageAuction, category, location);
        this.endOfAuction = endOfAuction;
        this.minimumSecretThreshold = minimumSecretThreshold;
    }

    //all get
    public Date getEndOfAuction() {
        return endOfAuction;
    }

    public Integer getMinimumSecretThreshold() {
        return minimumSecretThreshold;
    }

    //all set
    public void setEndOfAuction(Date endOfAuction) {
        this.endOfAuction = endOfAuction;
    }

    public void setMinimumSecretThreshold(Integer minimumSecretThreshold) {
        this.minimumSecretThreshold = minimumSecretThreshold;
    }
}