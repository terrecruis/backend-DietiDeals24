package it.backend.DietiDeals24.Service;

import it.backend.DietiDeals24.Dao.AuctionDAO;
import it.backend.DietiDeals24.Model.Auction;
import it.backend.DietiDeals24.Model.FixedTimeAuction;
import it.backend.DietiDeals24.Model.IncrementalAuction;
import it.backend.DietiDeals24.Model.Seller;
import it.backend.DietiDeals24.PostgresDao.AuctionPostgresDAO;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class AuctionService {

    final AuctionDAO<Auction> auction = new AuctionPostgresDAO();
    private static final Logger LOGGER = Logger.getLogger(AuctionService.class.getName());

    public AuctionService() {
        auction.updateStatusAuctionsDAO();
    }

    public List<Auction> getAuctionsService() {
        return auction.getAuctionsDAO();
    }

    public List<Auction> getMyAuctionsBuyerService(String email) {
        return auction.getMyAuctionsBuyerDAO(email);
    }

    public List<Auction> getMyAuctionsSellerService(String email) {
        return auction.getMyAuctionsSellerDAO(email);
    }

    public List<Auction> getAuctionsByCategoryAndPriceService(String toSearch, String startingPrice, String endingPrice, String category) {
        return auction.searchAuctionsDAO(toSearch, startingPrice, endingPrice, category);
    }

    public boolean createAuctionFixedTimeService(String json) {
        try {
            JsonObject jsonObject = parseJsonObject(json);
            FixedTimeAuction fixed = parseFixedTimeAuctionFromJson(jsonObject);
            return auction.insertFixedTimeAuctionDAO(fixed);
        } catch (ParseException e) {
            LOGGER.severe("Errore durante il parsing del JSON della creazione fixed time auction");
            return false;
        }
    }

    public boolean createIncrementalAuctionService(String json) {
        try {
            JsonObject jsonObject = parseJsonObject(json);
            IncrementalAuction incremental = parseIncrementalAuctionFromJson(jsonObject);

            System.out.println("Starting Price: " + incremental.getStartingPrice());
            System.out.println("Raising Threshold: " + incremental.getRaisingThreshold());
            System.out.println("Timer: " + incremental.getTimer());
            System.out.println("ID: " + incremental.getId());
            System.out.println("Creator: " + incremental.getCreator().getEmail());
            System.out.println("Participants: " + incremental.getParticipants());
            System.out.println("Title: " + incremental.getTitle());
            System.out.println("Description: " + incremental.getDescription());
            System.out.println("Image Auction: " + incremental.getImageAuction());
            System.out.println("Category: " + incremental.getCategory());
            System.out.println("Location: " + incremental.getLocation());
            System.out.println("Current Price: " + incremental.getCurrentPrice());

            return auction.insertIncrementalAuctionDAO(incremental);
        } catch (ParseException e) {
            LOGGER.severe("Errore durante il parsing del JSON della creazione incremental auction");
            return false;
        }
    }


    private JsonObject parseJsonObject(String json) {
        try (JsonReader reader = Json.createReader(new StringReader(json))) {
            return reader.readObject();
        }
    }

    private FixedTimeAuction parseFixedTimeAuctionFromJson(JsonObject jsonObject) throws ParseException {
        String id = auction.getNextAuctionId();
        JsonObject creatorObject = jsonObject.getJsonObject("creator");
        String creatorEmail = creatorObject.getString("email");
        Seller creator = new Seller(creatorEmail);

        String title = jsonObject.getString("title");
        System.out.println(title);
        String description = jsonObject.getString("description");
        System.out.println(description);
        String imageAuction = jsonObject.getString("imageAuction");
        System.out.println(imageAuction);
        String category = jsonObject.getString("category");
        System.out.println(category);
        String location = jsonObject.getString("location");
        System.out.println(location);
        BigDecimal currentPrice = jsonObject.getJsonNumber("currentPrice").bigDecimalValue();
        System.out.println(currentPrice);
        Date endOfAuction = new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject.getString("endOfAuction"));
        System.out.println(endOfAuction);
        BigDecimal minimumSecretThreshold = jsonObject.getJsonNumber("minimumSecretThreshold").bigDecimalValue();
        System.out.println(minimumSecretThreshold);

        return new FixedTimeAuction(id, creator, null, title, description, imageAuction, category, location, endOfAuction, minimumSecretThreshold, currentPrice);
    }

    private IncrementalAuction parseIncrementalAuctionFromJson(JsonObject jsonObject) throws ParseException {
        String id = auction.getNextAuctionId();
        System.out.println("id generato : " + id);
        JsonObject creatorObject = jsonObject.getJsonObject("creator");
        String creatorEmail = creatorObject.getString("email");
        Seller creator = new Seller(creatorEmail);

        String title = jsonObject.getString("title");
        String description = jsonObject.getString("description");
        String imageAuction = jsonObject.getString("imageAuction");
        String category = jsonObject.getString("category");
        String location = jsonObject.getString("location");
        BigDecimal currentPrice = jsonObject.getJsonNumber("currentPrice").bigDecimalValue();
        BigDecimal startingPrice = jsonObject.getJsonNumber("startingPrice").bigDecimalValue();
        BigDecimal raisingThreshold = jsonObject.getJsonNumber("raisingThreshold").bigDecimalValue();
        int timer = jsonObject.getInt("timer");

        return new IncrementalAuction(id, creator, null, title, description, imageAuction, category, location, startingPrice, raisingThreshold, timer, currentPrice);

    }
}
