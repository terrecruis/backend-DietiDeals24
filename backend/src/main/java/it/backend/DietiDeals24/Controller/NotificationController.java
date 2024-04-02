package it.backend.DietiDeals24.Controller;
import it.backend.DietiDeals24.Model.Notification;
import it.backend.DietiDeals24.Service.NotificationService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import java.util.List;



@Path("/notification")
public class NotificationController {

    NotificationService service = new NotificationService();


    @GET
    @Path("/{email}/buyer")
    @Produces("application/json")
    public List<Notification> getNotificationBuyer(@PathParam("email") String email) {
        return service.getNotificationBuyerService(email);
    }


    @GET
    @Path("/{email}/seller")
    @Produces("application/json")
    public List<Notification> getNotificationSeller(@PathParam("email") String email) {
        return service.getNotificationSellerService(email);
    }


}
