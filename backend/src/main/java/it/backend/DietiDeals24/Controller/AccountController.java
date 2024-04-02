package it.backend.DietiDeals24.Controller;
import it.backend.DietiDeals24.Service.AccountService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/account")
public class AccountController {

    final AccountService service = new AccountService();

    @POST
    @Path("/user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAccount(String json) {
        boolean success = service.addAccountService(json);

        if (success) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("credentials already exists!").build();
        }
    }


    @POST
    @Path("/premiumSeller")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response upgradePremiumAccount(String json) {
        if(service.upgradePremiumAccountService(json)) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("database error!").build();
        }
    }


    @GET
    @Path("/info/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInfoAccount(@PathParam("email") String email) {
        return Response.ok(service.getInfoAccountService(email)).build();
    }


    //funzione per modifica del profilo
    @PUT
    @Path("/{accountType}/modifyAccount")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateInfoAccount(String json, @PathParam("accountType") String accountType) {
        if(service.updateInfoAccountService(json, accountType)) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("database error!").build();
        }
    }

}
