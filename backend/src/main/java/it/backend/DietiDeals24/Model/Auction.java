package it.backend.DietiDeals24.Model;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class Auction {

    private String id;

    private Seller creator;
    
    private List<Buyer> participants;

    private String title;

    private String description;

    private String imageAuction;

    private String category;

    private String location;

    private BigDecimal currentPrice;

    //full constructor
    protected Auction(String id, Seller creator, List<Buyer> partenecipants, String title, String description, String imageAuction, String category, String location, BigDecimal currentPrice) {
        this.id = id;
        this.creator = creator;
        this.participants = partenecipants;
        this.title = title;
        this.description = description;
        this.imageAuction = imageAuction;
        this.category = category;
        this.location = location;
        this.currentPrice = currentPrice;
    }

    protected Auction(String id){
        this.id = id;
    }

    //all get
    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public String getId() {
        return id;
    }
    public Seller getCreator() {
        return creator;
    }

    public List<Buyer> getParticipants() {
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
    public void setId(String id) {
        this.id = id;
    }
    public void setCreator(Seller creator) {
        this.creator = creator;
    }

    public void setParticipants(List<Buyer> participants) {
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
