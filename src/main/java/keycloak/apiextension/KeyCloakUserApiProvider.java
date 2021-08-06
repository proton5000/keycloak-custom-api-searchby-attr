package keycloak.apiextension;

import keycloak.apiextension.services.CustomSmsService;
import keycloak.apiextension.services.SmsService;
import org.apache.http.HttpStatus;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.utils.MediaType;

import javax.ws.rs.*;
import java.io.IOException;

public class KeyCloakUserApiProvider implements RealmResourceProvider {
    private final KeycloakSession session;
    private final SmsService smsService;

    public KeyCloakUserApiProvider(KeycloakSession session) {
        this.session = session;
        this.smsService = new CustomSmsService();
    }

    public void close() {
    }

    public Object getResource() {
        return this;
    }

    @GET
    @Path("send-by-phone")
    @NoCache
    @Produces({ MediaType.APPLICATION_JSON })
    @Encoded
    public int searchUsersByAttribute(@QueryParam("phone") String phone) throws IOException {
        smsService.send(phone, "The otp code was send to number: " + phone);
        return HttpStatus.SC_OK;
    }
}
