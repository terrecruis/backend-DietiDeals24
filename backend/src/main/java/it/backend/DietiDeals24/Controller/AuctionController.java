package it.backend.DietiDeals24.Controller;
import it.backend.DietiDeals24.Model.Auction;
import it.backend.DietiDeals24.Service.AuctionService;
import jakarta.ws.rs.*;
import java.util.ArrayList;
import java.util.List;

@Path("/auction")
public class AuctionController {

    final AuctionService service = new AuctionService();
    List<Auction> auctions = new ArrayList<>();

    @GET
    @Path("/home")
    @Produces("application/json")
    public List<Auction> getAuctions() {
        auctions = service.getAuctionsService();
        return auctions;
    }

    @GET
    @Path("/{email}/buyer/myAuction")
    @Produces("application/json")
    public List<Auction> getMyAuctionsBuyer(@PathParam("email") String email) {
        return service.getMyAuctionsBuyerService(email);
    }

    @GET
    @Path("/{email}/seller/myAuction")
    @Produces("application/json")
    public List<Auction> getMyAuctionsSeller(@PathParam("email") String email) {
        return service.getMyAuctionsSellerService(email);
    }


    @GET
    @Path("/home/search")
    @Produces("application/json")
    public List<Auction> getAuctionsByCategoryAndPrice(
            @QueryParam("toSearch") String toSearch,
            @QueryParam("category") String category,
            @QueryParam("startingPrice") String startingPrice,
            @QueryParam("endingPrice") String endingPrice) {
        return service.getAuctionsByCategoryAndPriceService(toSearch, startingPrice, endingPrice, category);
    }

}
