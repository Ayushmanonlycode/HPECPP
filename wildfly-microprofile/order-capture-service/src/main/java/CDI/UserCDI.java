package CDI;


import Client.UserClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class UserCDI {

    @Inject
    @RestClient
    UserClient userClient;


    @Retry(maxRetries = 3)
    @Timeout(2000)
    @CircuitBreaker
    @Fallback(fallbackMethod = "userFallback")
    public Response checkUser(String id) {
        return userClient.checkUser(id);
    }

    public Response userFallback(String id) {
        return Response.status(503).build();
    }



}
