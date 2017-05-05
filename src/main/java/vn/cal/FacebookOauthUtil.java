package vn.cal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import java.io.IOException;
import java.util.Map;

	
public class FacebookOauthUtil {

	static final String clientId = "1232613673433393";
	static final String clientSecret = "a8fc664b2b6532f7e8f9f06499012b30";
	static final OAuth20Service service = new ServiceBuilder()
			.apiKey(clientId)
			.apiSecret(clientSecret)
			.callback("http://localhost:8080/hienmaunhandao/")
			.build(FacebookApi.instance());

	public static OAuth20Service getFBOauthService() {
		return service;
	}

	public static String getAuthorizationUrl() {

		return service.getAuthorizationUrl();
	}

	public static String getFacebookID(OAuth2AccessToken accessToken) {

		final OAuthRequest request = new OAuthRequest(Verb.GET, "https://graph.facebook.com/v2.5/me", service);
		service.signRequest(accessToken, request);
		final Response response = request.send();
		if (response.getCode() == 200) {
			String body = response.getBody();

			ObjectMapper mapper = new ObjectMapper();

			Map<String, String> map;
			// convert JSON string to Map
			try {
				map = mapper.readValue(body, new TypeReference<Map<String, String>>() {
				});
				return map.get("id");
			} catch (IOException e) {
				System.out.println(e);
			}
		}
		return null;
	}
}
