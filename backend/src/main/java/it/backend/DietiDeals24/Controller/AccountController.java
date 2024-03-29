package it.backend.DietiDeals24.Controller;
import it.backend.DietiDeals24.Service.AuthService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/account")
public class AccountController {


    final AuthService service = new AuthService();

    @POST
    @Path("/user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAccount(String json) {
        return service.addAccountService(json);
    }


    @GET
    @Path("/premiumSeller")
    @Produces(MediaType.APPLICATION_JSON)
    public Response upgradePremiumAccount(String json) {
        if(service.upgradePremiumAccountService(json)) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("errore connessione database!").build();
        }
    }
}
