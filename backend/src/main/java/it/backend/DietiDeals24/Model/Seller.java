package it.backend.DietiDeals24.Model;
import java.util.List;

public class Seller extends Account {

    private List<Auction> auctionsGenerated;


    public Seller(String email) {
        super(email);
    }

    public Seller(String fullName, String imageAccount, String email, String description, String telephoneNumber, String country) {
        super(fullName, imageAccount, email, description, telephoneNumber, country);
    }
    //get and set
    public List<Auction> getAuctionsGenerated() {
        return auctionsGenerated;
    }

    public void setAuctionsGenerated(List<Auction> auctionsGenerated) {
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
