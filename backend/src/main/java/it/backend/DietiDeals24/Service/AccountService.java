package it.backend.DietiDeals24.Service;
import it.backend.DietiDeals24.Dao.AccountDAO;
import it.backend.DietiDeals24.Model.Account;
import it.backend.DietiDeals24.PostgresDao.AccountPostgresDAO;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import java.io.StringReader;

public class AccountService {

    private final AccountDAO<Account> account = new AccountPostgresDAO();

    public boolean addAccountService(String json) {
        try {
            JsonObject jsonObject = parseJson(json);
            String[] fields = extractFields(jsonObject);
            return account.addAccountDAO(fields[0], fields[1], fields[2]);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean upgradePremiumAccountService(String json) {
        try {
            JsonObject jsonObject = parseJson(json);
            String[] fields = extractFields(jsonObject);
            return account.upgradePremiumAccountDAO(fields[0], fields[1], fields[2]);
        } catch (Exception e) {
            return false;
        }
    }

    public Account getInfoAccountService(String email) {
        return account.getInfoAccountDAO(email);
    }


    public boolean updateInfoAccountService(String json, String accountType) {
        try {
            JsonObject jsonObject = parseJson(json);
            String[] fields = extractAllAccountFields(jsonObject);
            if(accountType.equals("buyer"))
                return account.updateInfoBuyerAccountDAO(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5], fields[6]);
            else{
                return account.updateInfoSellerAccountDAO(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5], fields[6]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    private String[] extractFields(JsonObject jsonObject) {
        String email = jsonObject.getString("email");
        String fullname = jsonObject.getString("fullname");
        String telephoneNumber = jsonObject.getString("telephoneNumber");
        return new String[]{email, fullname, telephoneNumber};
    }



    private String[] extractAllAccountFields(JsonObject jsonObject) {
        String[] fields = extractFields(jsonObject);
        String country = jsonObject.getString("nationality");
        String description = jsonObject.getString("description");
        String link1 = jsonObject.getString("link1");
        String link2 = jsonObject.getString("link2");
        return new String[]{fields[0], fields[1], fields[2], country, description, link1, link2};
    }



    private JsonObject parseJson(String json) {
        try (JsonReader reader = Json.createReader(new StringReader(json))) {
            return reader.readObject();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JSON format", e);
        }
    }
}