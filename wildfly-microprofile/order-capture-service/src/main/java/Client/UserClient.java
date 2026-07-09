package Client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;

@Path("/users")
@RegisterRestClient(configKey = "user-service")
@RegisterClientHeaders(JwtPropagationHeadersFactory.class)
public interface UserClient {

    @GET
    @Path("/check/{id}")
    Response checkUser(
            @PathParam("id") String id
    );
}