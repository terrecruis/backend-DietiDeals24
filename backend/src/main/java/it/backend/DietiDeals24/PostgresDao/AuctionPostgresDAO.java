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

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(AuctionPostgresDAO.class.getName());
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
            LOGGER.log(java.util.logging.Level.SEVERE, "Errore durante il recupero delle aste", e);
        }
        return auctions;
    }

    @Override
    public List<Auction> searchAuctionsDAO(String toSearch, String startPrice, String endingPrice, String category) {
        List<Auction> auctions = new ArrayList<>();
        query = buildQuery(toSearch, startPrice, endingPrice, category);
        System.out.println("La query è: " + query);

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            setParameters(statement, toSearch, startPrice, endingPrice, category);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                auctions.add(createAuctionFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Errore durante la ricerca delle aste", e);
        }
        return auctions;
    }

    private String buildQuery(String toSearch, String startPrice, String endingPrice, String category) {
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM VistaAsteAttiveConPuntata WHERE 1=1");

        if (toSearch != null && !toSearch.isEmpty()) {
            queryBuilder.append(" AND titolo ILIKE ?");
        }

        if (startPrice != null && !startPrice.isEmpty() && endingPrice != null && !endingPrice.isEmpty()) {
            queryBuilder.append(" AND prezzoMassimo BETWEEN ? AND ?");
        }

        if (category != null && !category.isEmpty()) {
            queryBuilder.append(" AND categoria ILIKE ?");
        }

        return queryBuilder.toString();
    }

    private void setParameters(PreparedStatement statement, String toSearch, String startPrice, String endingPrice, String category) throws SQLException {
        int parameterIndex = 1;

        if (toSearch != null && !toSearch.isEmpty()) {
            statement.setString(parameterIndex++, "%" + toSearch + "%");
        }

        if (startPrice != null && !startPrice.isEmpty() && endingPrice != null && !endingPrice.isEmpty()) {
            statement.setBigDecimal(parameterIndex++, new BigDecimal(startPrice));
            statement.setBigDecimal(parameterIndex++, new BigDecimal(endingPrice));
        }

        if (category != null && !category.isEmpty()) {
            statement.setString(parameterIndex, "%" + category + "%");
        }
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
            LOGGER.log(java.util.logging.Level.SEVERE, "Errore durante il recupero delle aste", e);
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
            LOGGER.log(java.util.logging.Level.SEVERE, "Errore durante il recupero delle mie aste", e);
        }
        return auctions;
    }

    @Override
    public boolean insertFixedTimeAuctionDAO(FixedTimeAuction auction) {
        query = "INSERT INTO ASTA (idAsta, emailVenditore, titolo, descrizione, foto, categoria, luogo, tipoAsta, dataScadenza, sogliaMinimaSegreta, statoAsta) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, 'tempoFisso', ?, ?, 'in corso')";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, auction.getId());
            statement.setString(2, auction.getCreator().getEmail());
            statement.setString(3, auction.getTitle());
            statement.setString(4, auction.getDescription());
            statement.setString(5, auction.getImageAuction());
            statement.setString(6, auction.getCategory());
            statement.setString(7, auction.getLocation());
            statement.setDate(8, new java.sql.Date(auction.getEndOfAuction().getTime()));
            statement.setBigDecimal(9, auction.getMinimumSecretThreshold());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Errore durante l'inserimento dell'asta a tempo fisso", e);
            return false;
        }
    }

    @Override
    public boolean insertIncrementalAuctionDAO(IncrementalAuction auction) {
        query = "INSERT INTO ASTA (idAsta, titolo, descrizione, foto, categoria, tipoAsta, timer, sogliaRialzo, basePubblica, StatoAsta, emailVenditore, luogo) " +
                "VALUES (?, ?, ?, ?, ?, 'incrementale', ?, ?, ?, 'in corso', ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, auction.getId());
            statement.setString(2, auction.getTitle());
            statement.setString(3, auction.getDescription());
            statement.setString(4, auction.getImageAuction());
            statement.setString(5, auction.getCategory());
            statement.setInt(6, auction.getTimer());
            statement.setBigDecimal(7, auction.getRaisingThreshold());
            statement.setBigDecimal(8, auction.getStartingPrice());
            statement.setString(9, auction.getCreator().getEmail());
            statement.setString(10, auction.getLocation());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Errore durante l'inserimento dell'asta incrementale", e);
            return false;
        }
    }


    /*
     * Sfrutta le procedure di aggiornamento a cascata implementate nel datbase
     * cosi da alleggerire il carico di lavoro del server e migliorare le prestazioni.
     * Non solo aggiorna lo stato delle aste ma anche le notifiche associate ad un asta specifica.
     */
    @Override
    public void updateStatusAuctionsDAO() {
        query = "CALL aggiornastatoasta()";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new QueryExecutionException("Errore durante l'aggiornamento dello stato delle aste", e);
        }
    }


    public String getNextAuctionId() {
        String nextId = null;
        query = "SELECT MAX(idAsta) AS maxId FROM ASTA";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                String maxId = resultSet.getString("maxId");
                if (maxId != null) {
                    String[] parts = maxId.split("-");
                    int idNumber = Integer.parseInt(parts[1]) + 1;
                    nextId = "ID-" + String.format("%03d", idNumber);
                } else {
                    nextId = "ID-001";
                }
            }
        } catch (SQLException e) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Errore durante la generazione del prossimo ID asta", e);
        }

        return nextId;
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
            // Format the date string before creating FixedTimeAuction instance
            Date endOfAuction = resultSet.getDate("dataScadenza");
            BigDecimal minimumSecretThreshold = resultSet.getBigDecimal("sogliaMinimaSegreta");
            return new FixedTimeAuction(id, new Seller(seller), null, title, description, imageAuction, category, location, endOfAuction, minimumSecretThreshold, currentPrice);
        }
    }
}