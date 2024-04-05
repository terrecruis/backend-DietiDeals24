package it.backend.DietiDeals24.Controller;

import it.backend.DietiDeals24.Service.BetService;
import it.backend.DietiDeals24.filter.RequireJWTAuthentication;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;


@Path("/bet")
public class BetController {

    final BetService betService = new BetService();

    @POST
    @RequireJWTAuthentication
    @Path("/makeBet")
    @Consumes("application/json")
    @Produces("application/json")
    public Response makeBet(String json) {
        if (betService.makeBetService(json)) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("database error!").build();
        }
    }

}
