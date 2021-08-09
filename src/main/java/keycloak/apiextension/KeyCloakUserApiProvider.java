package keycloak.apiextension;

import keycloak.apiextension.services.CustomSmsService;
import keycloak.apiextension.services.SmsService;
import org.apache.http.HttpStatus;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.utils.MediaType;

import javax.ws.rs.*;
import java.io.IOException;

public class KeyCloakUserApiProvider implements RealmResourceProvider {

    private final KeycloakSession session;
    private final SmsService smsService;
    private final AuthenticationManager.AuthResult auth;

    public KeyCloakUserApiProvider(KeycloakSession session) {
        this.session = session;
        this.smsService = new CustomSmsService();
        this.auth = new AppAuthManager.BearerTokenAuthenticator(session).authenticate();
    }

    public void close() {
    }

    public Object getResource() {
        return this;
    }

    private void checkAuthentication() {
        if (auth == null) {
            throw new NotAuthorizedException("Bearer");
        } else if (auth.getToken().getRealmAccess() == null || !auth.getToken().getRealmAccess().isUserInRole("CLIENT")) {
            throw new ForbiddenException("Does not have realm SERVICE role");
        }
    }

    @GET
    @Path("send-by-phone")
    @NoCache
    @Produces({ MediaType.APPLICATION_JSON })
    @Encoded
    public int searchUsersByAttribute(@QueryParam("phone") String phone) throws IOException {
        checkAuthentication();
        smsService.send(phone, "The otp code was send to number: " + phone);
        return HttpStatus.SC_OK;
    }
}
