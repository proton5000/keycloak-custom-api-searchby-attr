package keycloak.apiextension.services;

import java.io.IOException;

public interface SmsService {
	void send(String phoneNumber, String message) throws IOException;
}
