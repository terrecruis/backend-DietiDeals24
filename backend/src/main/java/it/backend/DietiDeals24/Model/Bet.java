package it.backend.DietiDeals24.Model;

public class Bet {

    private Buyer buyerAssociated;

    private Auction auctionAssociated;

    private Integer betValue;

    public Bet(Buyer buyerAssociated, Auction auctionAssociated, Integer betValue) {
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

    public Integer getBetValue() {
    	return this.betValue;
    }

    public void setBuyerAssociated(Buyer buyerAssociated) {
    	this.buyerAssociated = buyerAssociated;
    }

    public void setAuctionAssociated(Auction auctionAssociated) {
    	this.auctionAssociated = auctionAssociated;
    }

    public void setBetValue(Integer betValue) {
    	this.betValue = betValue;
    }
}
