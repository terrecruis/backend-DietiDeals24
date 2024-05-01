package it.backend.DietiDeals24.Controller;
import it.backend.DietiDeals24.Model.Auction;
import it.backend.DietiDeals24.Service.AuctionService;
import it.backend.DietiDeals24.filter.RequireJWTAuthentication;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("/auction")
public class AuctionController {

    final AuctionService service = new AuctionService();
    List<Auction> auctions = new ArrayList<>();

    @GET
    //@RequireJWTAuthentication
    @Path("/home")
    @Produces("application/json")
    public List<Auction> getAuctions() {
        auctions = service.getAuctionsService();
        return auctions;
    }

    @GET
    @RequireJWTAuthentication
    @Path("/{email}/buyer/myAuction")
    @Produces("application/json")
    public List<Auction> getMyAuctionsBuyer(@PathParam("email") String email) {
        return service.getMyAuctionsBuyerService(email);
    }

    @GET
    @RequireJWTAuthentication
    @Path("/{email}/seller/myAuction")
    @Produces("application/json")
    public List<Auction> getMyAuctionsSeller(@PathParam("email") String email) {
        return service.getMyAuctionsSellerService(email);
    }


    @GET
    @RequireJWTAuthentication
    @Path("/home/search")
    @Produces("application/json")
    public List<Auction> getAuctionsByCategoryAndPrice(
            @QueryParam("toSearch") String toSearch,
            @QueryParam("category") String category,
            @QueryParam("startingPrice") String startingPrice,
            @QueryParam("endingPrice") String endingPrice) {
        return service.getAuctionsByCategoryAndPriceService(toSearch, startingPrice, endingPrice, category);
    }


    @PUT
    @Path("/createAuction/fixedTime")
    @RequireJWTAuthentication
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAuctionFixedTime(String json) {
        if (service.createAuctionFixedTimeService(json)) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("database error!").build();
        }
    }


    @PUT
    @Path("/createAuction/incremental")
    @RequireJWTAuthentication
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAuctionIncremental(String json) {
        if (service.createIncrementalAuctionService(json)) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("database error!").build();
        }
    }





}
