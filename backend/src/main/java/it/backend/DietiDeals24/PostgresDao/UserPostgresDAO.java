package it.backend.DietiDeals24.PostgresDao;
import it.backend.DietiDeals24.Dao.UserDAO;
import it.backend.DietiDeals24.DbConnection.DbConnection;
import jakarta.ws.rs.container.ConnectionCallback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserPostgresDAO implements UserDAO {

    Connection connection;

    public UserPostgresDAO(){
        try {
            connection = DbConnection.getInstance().getConnection();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean login(String email, String password) {
        //cerca nel database l'utente con la mail e la password
        PreparedStatement user;
        try {
            user = connection.prepareStatement("SELECT * FROM compratore WHERE email = ? AND password = ?");
            user.setString(1, email);
            user.setString(2, password);
            return user.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}