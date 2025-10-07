package ressourcesRest;

import entities.Credentials;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.security.Key;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Path("authentication")
public class AuthenticationEndPoint {

    @Context
    private UriInfo uriInfo;

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authenticateUser(@FormParam("username") String username,
                                     @FormParam("password") String password) {
        try {
            authenticate(username, password);
            String token = issueToken(username);
            return Response.ok(token).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @POST
    @Path("json")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(Credentials cred) {
        try {
            authenticate(cred.getUsername(), cred.getPassword());
            String token = issueToken(cred.getUsername());
            return Response.ok(token).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    private void authenticate(String username, String password) {
        // TODO: validate against real user store; for workshop accept any non-empty
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new RuntimeException("Invalid credentials");
        }
    }

    private String issueToken(String username) {
        String keyString = "simplekey";
        Key key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "DES");

        String jwtToken = Jwts.builder()
                .setSubject(username)
                .setIssuer(uriInfo.getAbsolutePath().toString())
                .setIssuedAt(new Date())
                .setExpiration(toDate(LocalDateTime.now().plusMinutes(15)))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        return jwtToken;
    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
