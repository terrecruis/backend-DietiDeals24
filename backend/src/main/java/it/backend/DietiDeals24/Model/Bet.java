package it.backend.DietiDeals24.Model;

import java.math.BigDecimal;

public class Bet {

    private Buyer buyerAssociated;

    private Auction auctionAssociated;

    private BigDecimal betValue;

    public Bet(Buyer buyerAssociated, Auction auctionAssociated, BigDecimal betValue) {
    	this.buyerAssociated = buyerAssociated;
    	this.auctionAssociated = auctionAssociated;
    	this.betValue = betValue;
    }
    //all get and set
    public Buyer getBuyerAssociated() {
    	return this.buyerAssociated;
    }

    public Auction getAuctionAssociated() {
    	return this.auctionAssociated;
    }

    public BigDecimal getBetValue() {
    	return this.betValue;
    }

    public void setBuyerAssociated(Buyer buyerAssociated) {
    	this.buyerAssociated = buyerAssociated;
    }

    public void setAuctionAssociated(Auction auctionAssociated) {
    	this.auctionAssociated = auctionAssociated;
    }

    public void setBetValue(BigDecimal betValue) {
    	this.betValue = betValue;
    }
}
