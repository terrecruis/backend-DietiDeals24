package it.backend.DietiDeals24.Controller;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import it.backend.DietiDeals24.Service.AccountService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Path("/auth")
public class AuthController {

    private static final Logger LOGGER = Logger.getLogger(AuthController.class.getName());
    private static final String ISSUER = "todo-rest-api";
    private static Algorithm algorithm;
    private static JWTVerifier verifier;
    private final AccountService accountService = new AccountService();

    static {
        loadSecretKey();
    }

    private static void loadSecretKey() {
        Properties prop = new Properties();
        try (InputStream input = Files.newInputStream(Paths.get("configuration.properties"))) {
            prop.load(input);
            String secretKey = prop.getProperty("jwt.secret");
            algorithm = Algorithm.HMAC256(secretKey);
            verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
        } catch (IOException e) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Error reading configuration.properties file", e);
        }
    }

    public String getUsernameClaim(String token) {
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getClaim("username").asString();
        } catch (JWTVerificationException e) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Error verifying token", e);
            return null;
        }
    }


    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(String json) {
        try {
            String email = accountService.getUsernameFromJson(json);
                String token = createJWT(email, TimeUnit.DAYS.toMillis(365)); // days
                if(validateToken(token))
                    return Response.ok().entity(token).build();
                else
                    return Response.status(Response.Status.UNAUTHORIZED).build();
            }  catch (Exception e) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Error login", e);
            return Response.serverError().build();
        }
    }


    private String createJWT(String username, long ttlMillis) {

        return JWT.create()
                .withIssuer(ISSUER)
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + ttlMillis))
                .withJWTId(UUID.randomUUID().toString())
                .sign(algorithm);
    }

    public static boolean validateToken(String token){
        try {
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Error verifying token", e);
            return false;
        }
    }

}