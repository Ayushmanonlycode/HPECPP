package Client;

import jakarta.ws.rs.core.MultivaluedMap;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

/**
 * order-capture-service calls other domain services (inventory-service,
 * user-service) via MP Rest Client. Those outgoing calls are brand-new
 * HTTP requests — they do NOT automatically carry the Authorization
 * header from the inbound request that triggered them. Services like
 * inventory-service are secured with @RolesAllowed, so an unauthenticated
 * internal call gets rejected with 403 Forbidden.
 *
 * Registering this factory via @RegisterClientHeaders makes MP Rest
 * Client copy the caller's Authorization header onto every outgoing
 * request made through that client, so the caller's identity/roles
 * propagate through to downstream services.
 */
public class JwtPropagationHeadersFactory implements ClientHeadersFactory {

    @Override
    public MultivaluedMap<String, String> update(
            MultivaluedMap<String, String> incomingHeaders,
            MultivaluedMap<String, String> clientOutgoingHeaders) {

        String authHeader = incomingHeaders.getFirst("Authorization");
        if (authHeader != null) {
            clientOutgoingHeaders.putSingle("Authorization", authHeader);
        }
        return clientOutgoingHeaders;
    }
}

