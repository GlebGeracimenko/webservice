package com.gleb.webservices;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;

/**
 * @author Khamylov Oleksandr
 */
public class ClientTest {

	public static void main(String[] args) {
		Client client = ClientBuilder.newClient();

		WebTarget webTarget = client.target("http://localhost:8088");

		Response response = webTarget.path("sessionid").request().get();
		String sessionid = response.readEntity(String.class);

		Invocation.Builder reqBuilder = webTarget.path("recommendations").queryParam("sessionId", sessionid).request();
//		reqBuilder.cookie(new Cookie("JSESSIONID", sessionid));
		response = reqBuilder.get();
		System.out.println(response);

		reqBuilder = webTarget.path("recommendations").queryParam("sessionId", sessionid).request();
//		reqBuilder.cookie(new Cookie("JSESSIONID", sessionid));
		response = reqBuilder.get();
		System.out.println(response);

		reqBuilder = webTarget.path("like").queryParam("sessionId", sessionid).request();
//		reqBuilder.cookie(new Cookie("JSESSIONID", sessionid));
		response = reqBuilder.post(Entity.text("1234"));
		System.out.println(response);

	}
}
