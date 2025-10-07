package filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.annotation.Priority;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Key;

@Provider
@Secured
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            abort(requestContext);
            return;
        }
        String token = authorizationHeader.substring("Bearer".length()).trim();
        try {
            String keyString = "simplekey";
            Key key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "DES");
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            // Optionally set security context or properties if needed
        } catch (Exception e) {
            abort(requestContext);
        }
    }

    private void abort(ContainerRequestContext requestContext) {
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
    }
}
