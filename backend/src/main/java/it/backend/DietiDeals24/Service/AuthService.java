package it.backend.DietiDeals24.Service;
import it.backend.DietiDeals24.Dao.AccountDAO;
import it.backend.DietiDeals24.Exception.QueryExecutionException;
import it.backend.DietiDeals24.PostgresDao.AccountPostgresDAO;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.ws.rs.core.Response;
import java.io.StringReader;

public class AuthService {

    final AccountDAO account = new AccountPostgresDAO();

    public Response addAccountService(String json){
        try (JsonReader reader = Json.createReader(new StringReader(json))) {
            JsonObject jsonObject = reader.readObject();

            // Extracting values from JsonObject
            String email = jsonObject.getString("email");
            String fullname = jsonObject.getString("fullname");
            String telephoneNumber = jsonObject.getString("telephoneNumber");

            boolean success = account.addAccountDAO(email, fullname, telephoneNumber);

            if (success) {
                return Response.ok().build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity("credentials already exists!").build();
            }
        }
        catch (QueryExecutionException e) {
            // Handle JSON parsing or other exceptions
            return Response.status(Response.Status.BAD_REQUEST).entity("Credential already exists!").build();
        } catch (Exception e) {
            // Handle JSON parsing or other exceptions
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid JSON format").build();
        }
    }

    public boolean upgradePremiumAccountService(String json) {
        try(JsonReader reader = Json.createReader(new StringReader(json))) {
            JsonObject jsonObject = reader.readObject();

            // Extracting values from JsonObject
            String email = jsonObject.getString("email");
            String fullname = jsonObject.getString("fullname");
            String telephoneNumber = jsonObject.getString("telephoneNumber");

            return account.upgradePremiumAccountDAO(email, fullname, telephoneNumber);
        }
        catch (QueryExecutionException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }


}

