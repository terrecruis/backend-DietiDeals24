package it.backend.DietiDeals24.Model;

import java.util.ArrayList;
import java.util.Date;

public class IncrementalAuction extends  Auction {

    private Integer startingPrice;

    private Float raisingThreshold;

    private Date timer;

    public IncrementalAuction(Seller creator, ArrayList<Buyer> partenecipants, String title, String description, String imageAuction, String category, String location, Integer startingPrice, Float raisingThreshold, Date timer) {
        super(creator, partenecipants, title, description, imageAuction, category, location);
        this.startingPrice = startingPrice;
        this.raisingThreshold = raisingThreshold;
        this.timer = timer;
    }


    //all get
    public Integer getStartingPrice() {
        return startingPrice;
    }

    public Float getRaisingThreshold() {
        return raisingThreshold;
    }

    public Date getTimer() {
        return timer;
    }

    //all set
    public void setStartingPrice(Integer startingPrice) {
        this.startingPrice = startingPrice;
    }

    public void setRaisingThreshold(Float raisingThreshold) {
        this.raisingThreshold = raisingThreshold;
    }

    public void setTimer(Date timer) {
        this.timer = timer;
    }
}
