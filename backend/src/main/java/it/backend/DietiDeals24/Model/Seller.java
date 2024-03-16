package it.backend.DietiDeals24.Model;

import java.util.ArrayList;

public class Seller extends Account {

    private ArrayList<Auction> auctionsGenerated;


    public Seller(String email) {
        super(email);
    }
    public Seller(String fullName, String imageAccount, String email, String password, String description, Integer telephoneNumber, String country, String AuctionsGenerated) {
        super(fullName, imageAccount, email, password, description, telephoneNumber, country);
        this.auctionsGenerated = new ArrayList<Auction>();
    }

    //get and set
    public ArrayList<Auction> getAuctionsGenerated() {
        return auctionsGenerated;
    }

    public void setAuctionsGenerated(ArrayList<Auction> auctionsGenerated) {
        this.auctionsGenerated = auctionsGenerated;
    }

    //add and remove
    public void addAuction(Auction auction){
        this.auctionsGenerated.add(auction);
    }

    public void removeAuction(Auction auction){
        this.auctionsGenerated.remove(auction);
    }

}
