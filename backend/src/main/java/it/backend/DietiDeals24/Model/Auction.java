package it.backend.DietiDeals24.Model;

import java.util.ArrayList;

public abstract class Auction {

    private Seller creator;

    private ArrayList<Buyer> partenecipants;

    private String title;

    private String description;

    private String imageAuction;

    private String category;

    private String location;


    //full constructor
    public Auction(Seller creator, ArrayList<Buyer> partenecipants, String title, String description, String imageAuction, String category, String location) {
        this.creator = creator;
        this.partenecipants = partenecipants;
        this.title = title;
        this.description = description;
        this.imageAuction = imageAuction;
        this.category = category;
        this.location = location;
    }


    //all get
    public Seller getCreator() {
        return creator;
    }

    public ArrayList<Buyer> getPartenecipants() {
        return partenecipants;
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
    public void setCreator(Seller creator) {
        this.creator = creator;
    }

    public void setPartenecipants(ArrayList<Buyer> partenecipants) {
        this.partenecipants = partenecipants;
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
