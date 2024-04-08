package it.backend.DietiDeals24.Service;
import it.backend.DietiDeals24.Dao.BetDao;
import it.backend.DietiDeals24.PostgresDao.BetPostgresDAO;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.StringReader;
import java.math.BigDecimal;

public class BetService {

    final BetDao betDao = new BetPostgresDAO();

    public boolean makeBetService(String json) {

        try(JsonReader jsonReader = Json.createReader(new StringReader(json))) {
            JsonObject jsonObject = jsonReader.readObject();
            String idAuction = jsonObject.getString("idAuction");
            String emailBuyer = jsonObject.getString("emailBuyer");
            BigDecimal betValue = jsonObject.getJsonNumber("betValue").bigDecimalValue();
            return betDao.makeBetDAO(idAuction, emailBuyer, betValue);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

}