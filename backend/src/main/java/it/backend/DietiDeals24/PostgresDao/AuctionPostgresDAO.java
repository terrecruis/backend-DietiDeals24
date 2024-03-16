package it.backend.DietiDeals24.PostgresDao;
import it.backend.DietiDeals24.Dao.AuctionDAO;
import it.backend.DietiDeals24.DbConnection.DbConnection;
import it.backend.DietiDeals24.Exception.QueryExecutionException;
import it.backend.DietiDeals24.Model.*;
import it.backend.DietiDeals24.Model.FixedTimeAuction;
import it.backend.DietiDeals24.Model.IncrementalAuction;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuctionPostgresDAO implements AuctionDAO<Auction> {

    Connection connection;
    String query;

    public AuctionPostgresDAO() {
        try {
            connection = DbConnection.getInstance().getConnection();
        } catch (SQLException e) {
            throw new QueryExecutionException("Errore durante l'apertura della connessione al database", e);
        }
    }


    @Override
    public List<Auction> getAuctionsDAO() {
        List<Auction> auctions = new ArrayList<>();
        query = "SELECT * FROM VistaAsteAttiveConPuntata";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Auction auction = createAuctionFromResultSet(resultSet);
                auctions.add(auction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return auctions;
    }



    @Override
    public List<Auction> searchAuctionsDAO(String toSearch, String startPrice, String endingPrice, String category) {
        List<Auction> auctions = new ArrayList<>();
        query = "SELECT * FROM VistaAsteAttiveConPuntata WHERE 1=1";

        if (toSearch != null && !toSearch.isEmpty()) {
            query += " AND titolo ILIKE ?";
        }

        if (startPrice != null && !startPrice.isEmpty() && endingPrice != null && !endingPrice.isEmpty()) {
            query += " AND prezzoMassimo BETWEEN ? AND ?";
        }

        if (category != null && !category.isEmpty()) {
            query += " AND categoria ILIKE ?";
        }
        System.out.println("La query1 è : " + query);

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int parameterIndex = 1;
            if (toSearch != null && !toSearch.isEmpty()) {
                statement.setString(parameterIndex++, "%" + toSearch + "%");
            }

            if (startPrice != null && !startPrice.isEmpty() && endingPrice != null && !endingPrice.isEmpty()) {
                statement.setBigDecimal(parameterIndex++, new BigDecimal(startPrice));
                statement.setBigDecimal(parameterIndex++, new BigDecimal(endingPrice));
            }

            if (category != null && !category.isEmpty()) {
                statement.setString(parameterIndex++, "%" + category + "%");
            }

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                auctions.add(createAuctionFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return auctions;
    }




    @Override
    public List<Auction> getMyAuctionsBuyerDAO(String email) {
        List<Auction> auctions = new ArrayList<>();
        query = "SELECT a.*, p.importo AS prezzoMassimo " +
                "FROM ASTA a " +
                "JOIN PUNTATA p ON a.idAsta = p.idAsta " +
                "WHERE p.emailCompratore = ? AND a.statoasta IN ('in corso', 'conclusa') " +
                "UNION " +
                "SELECT a.*, p.importo AS prezzoMassimo " +
                "FROM ASTA a " +
                "JOIN PUNTATA p ON a.idAsta = p.idAsta " +
                "WHERE p.emailCompratore = ? AND a.statoasta = 'conclusa' " +
                "AND p.importo = (SELECT MAX(importo) FROM PUNTATA WHERE idAsta = a.idAsta)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, email);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                auctions.add(createAuctionFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return auctions;
    }

    @Override
    public List<Auction> getMyAuctionsSellerDAO(String email) {
        List<Auction> auctions = new ArrayList<>();
        query = "SELECT ASTA.*, COALESCE(puntataPiùAlta.puntataPiùAlta, 0) AS prezzoMassimo " +
                "FROM ASTA " +
                "LEFT JOIN ( " +
                "    SELECT idAsta, MAX(importo) AS puntataPiùAlta " +
                "    FROM PUNTATA " +
                "    GROUP BY idAsta " +
                ") AS puntataPiùAlta ON ASTA.idAsta = puntataPiùAlta.idAsta " +
                "WHERE ASTA.emailVenditore = ? AND ASTA.StatoAsta IN ('in corso', 'conclusa')";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                auctions.add(createAuctionFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return auctions;
    }





    // This method is used to create an auction from a result set
    private Auction createAuctionFromResultSet(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("idAsta");
        String title = resultSet.getString("titolo");
        String description = resultSet.getString("descrizione");
        String imageAuction = resultSet.getString("foto");
        String category = resultSet.getString("categoria");
        String location = resultSet.getString("luogo");
        String seller = resultSet.getString("emailVenditore");
        String type = resultSet.getString("tipoAsta");
        BigDecimal currentPrice = resultSet.getBigDecimal("prezzoMassimo");

        if (type.equals("incrementale")) {
            BigDecimal startingPrice = resultSet.getBigDecimal("basePubblica");
            BigDecimal raisingThreshold = resultSet.getBigDecimal("sogliaRialzo");
            Integer timer = resultSet.getInt("timer");
            return new IncrementalAuction(id, new Seller(seller), null, title, description, imageAuction, category, location, startingPrice, raisingThreshold, timer, currentPrice);
        } else {
            Date endOfAuction = resultSet.getDate("dataScadenza");
            BigDecimal minimumSecretThreshold = resultSet.getBigDecimal("sogliaMinimaSegreta");
            return new FixedTimeAuction(id, new Seller(seller), null, title, description, imageAuction, category, location, endOfAuction, minimumSecretThreshold, currentPrice);
        }
    }

}
