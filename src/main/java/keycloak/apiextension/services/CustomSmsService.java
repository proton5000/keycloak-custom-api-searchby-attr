package keycloak.apiextension.services;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.util.Collections;

public class CustomSmsService implements SmsService {

	private static final Logger LOG = Logger.getLogger(CustomSmsService.class);

	private static final String SEND_OTP_URL = "https://oauth2.varus.ua/rest/otp/send";

	@Override
	public void send(String phoneNumber, String message) throws IOException {
		LOG.info(String.format("The phone number is: %s", phoneNumber));
		sendOtp(phoneNumber);
		LOG.info(String.format("The sms message is: %s", message));
	}

	private void sendOtp(String mobileNumber) throws IOException {

		HttpPost post = new HttpPost(SEND_OTP_URL);
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");

		post.setEntity(new UrlEncodedFormEntity(Collections.singletonList(new BasicNameValuePair("phone", mobileNumber))));

		try (CloseableHttpClient httpClient = HttpClients.createDefault();
			 CloseableHttpResponse response = httpClient.execute(post)) {

			LOG.info("Send otp code. Response: " + EntityUtils.toString(response.getEntity()));
		}
	}
}
