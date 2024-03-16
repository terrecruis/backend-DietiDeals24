package it.backend.DietiDeals24.Controller;
import it.backend.DietiDeals24.Service.AuthService;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Priority(3)
@Path("/auth")
public class AuthController {

    final AuthService service = new AuthService();

    @POST
    @Path("/user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAccount(String json) {
        return service.addAccountService(json);
    }

}
