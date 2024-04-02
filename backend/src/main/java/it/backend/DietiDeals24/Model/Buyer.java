package it.backend.DietiDeals24.Model;

import java.util.ArrayList;
import java.util.List;

public class Buyer extends Account{

    private ArrayList<Auction> followedAuctions;

    //buyer with followed auctions
    public Buyer(String fullName, String imageAccount, String email, String description, Integer telephoneNumber, String country,ArrayList<Auction> followedAuctions) {
        super(fullName, imageAccount, email, description, telephoneNumber, country);
        this.followedAuctions = followedAuctions;
    }

    //buyer without followed auctions
    public Buyer(String fullName, String imageAccount, String email, String password, String description, Integer telephoneNumber, String country) {
        super(fullName, imageAccount, email, description, telephoneNumber, country);
        this.followedAuctions = new ArrayList<>();
    }

    public Buyer(String email) {
        super(email);
    }

    //get set followed auctions
    public List<Auction> getFollowedAuctions() {
        return followedAuctions;
    }

    public void setFollowedAuctions(ArrayList<Auction> followedAuctions) {
        this.followedAuctions = followedAuctions;
    }

    //add and remove followed auctions
    public void addFollowedAuction(Auction auction){
        this.followedAuctions.add(auction);
    }

    public void removeFollowedAuction(Auction auction){
        this.followedAuctions.remove(auction);
    }
}
