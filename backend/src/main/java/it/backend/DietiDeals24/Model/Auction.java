package it.backend.DietiDeals24.Model;

import java.math.BigDecimal;
import java.util.ArrayList;

public abstract class Auction {

    private String Id;
    private Seller creator;
    
    private ArrayList<Buyer> participants;

    private String title;

    private String description;

    private String imageAuction;

    private String category;

    private String location;

    private BigDecimal currentPrice;

    //full constructor
    protected Auction(String Id, Seller creator, ArrayList<Buyer> partenecipants, String title, String description, String imageAuction, String category, String location, BigDecimal currentPrice) {
        this.Id = Id;
        this.creator = creator;
        this.participants = partenecipants;
        this.title = title;
        this.description = description;
        this.imageAuction = imageAuction;
        this.category = category;
        this.location = location;
        this.currentPrice = currentPrice;
    }


    //all get
    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public String getId() {
        return Id;
    }
    public Seller getCreator() {
        return creator;
    }

    public ArrayList<Buyer> getParticipants() {
        return participants;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageAuction() {
        return imageAuction;
    }

    public String getCategory() {
        return category;
    }

    public String getLocation() {
        return location;
    }



    //all set
    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }
    public void setId(String Id) {
        this.Id = Id;
    }
    public void setCreator(Seller creator) {
        this.creator = creator;
    }

    public void setParticipants(ArrayList<Buyer> participants) {
        this.participants = participants;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageAuction(String imageAuction) {
        this.imageAuction = imageAuction;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLocation(String location) {
        this.location = location;
    }




}
