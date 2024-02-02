package it.backend.DietiDeals24.Controller;

import it.backend.DietiDeals24.Dao.UserDAO;
import it.backend.DietiDeals24.PostgresDao.UserPostgresDAO;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.StringReader;

@Path("/auth")
public class AuthController {

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(String json) {

        try (JsonReader reader = Json.createReader(new StringReader(json))) {
            JsonObject jsonObject = reader.readObject();

            // Extracting values from JsonObject
            String email = jsonObject.getString("email");
            String password = jsonObject.getString("password");

            // Call the DAO method to check if the user exists
            UserDAO userDAO = new UserPostgresDAO();
            boolean userExists = userDAO.login(email, password);

            if (userExists) {
                return Response.ok().build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
            }

        } catch (Exception e) {
            // Handle JSON parsing or other exceptions
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid JSON format").build();
        }
    }
}
