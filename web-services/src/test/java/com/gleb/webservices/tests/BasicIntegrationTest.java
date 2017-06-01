package com.gleb.webservices.tests;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteAction;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gleb.dao.cassandra.BasicCQLCassandraDAO;
import com.gleb.dao.elasticsearch.ElasticsearchClient;
import com.gleb.dao.objects.DBUser;
import com.gleb.webservices.bo.LoginPassword;

public class BasicIntegrationTest {

	private static Server server;
	public static String BASE_URL = "http://localhost:9090/";
	protected static DBUser BASIC_USER;
	private static JerseyClient client;

	private static Logger logger = LoggerFactory.getLogger(BasicIntegrationTest.class);
	
	@Autowired
	protected BasicCQLCassandraDAO basicCQLDAO;

	@Autowired
	protected ElasticsearchClient clientHolder;

	protected void storeBatchElastic(String type, String batchName) throws IOException {
		File file = new File("src/test/resources/test-batches", batchName);
		logger.info("Start batching: "+batchName+", type: "+type);
		ObjectMapper jsonMapper = new ObjectMapper();
		JavaType javaType = jsonMapper.getTypeFactory().constructCollectionType(List.class, JsonNode.class);
		List<JsonNode> batchObjects = jsonMapper.readValue(file, javaType);
		for(JsonNode json : batchObjects) {
			IndexResponse response = clientHolder.getClient().prepareIndex().setSource(json.toString()).setType(type).setIndex(clientHolder.getIndexName()).get();
			logger.info("\tindexed object: id "+response.getId()+", source: "+json.toString());
		}
	}

	@BeforeClass
	public static void init() throws Exception {

		server = new Server(9090);
		server.setStopAtShutdown(true);
		WebAppContext webAppContext = new WebAppContext();
		webAppContext.setResourceBase("src/test/resources");
		webAppContext.setDescriptor("src/test/resources/test-web.xml");
		webAppContext.setClassLoader(BasicIntegrationTest.class.getClassLoader());
		server.addHandler(webAppContext);
		server.start();
	}

	protected void truncateCassandra(String columnFamily) {
		basicCQLDAO.getSession().execute("truncate " + columnFamily);
	}

	protected void truncateElasticsearchType(String type) {
	 // We need to wait the sync for elastic. it is async - so just dirty hack 
		try {
		    Thread.sleep(1000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		SearchRequestBuilder requestBuilder = clientHolder.getClient().prepareSearch(clientHolder.getIndexName()).setTypes(type)
			.setSearchType(SearchType.DFS_QUERY_AND_FETCH).setQuery(QueryBuilders.matchAllQuery()).setNoFields();
		SearchResponse response = requestBuilder.setExplain(true).execute().actionGet();
		SearchHits hits = response.getHits();
		if (hits.getTotalHits() > 0) {
		    BulkRequestBuilder bulk = clientHolder.getClient().prepareBulk();
		    for (SearchHit hit : hits) {
			DeleteRequestBuilder builder = new DeleteRequestBuilder(clientHolder.getClient(), DeleteAction.INSTANCE);
			builder.setId(hit.getId());
			builder.setIndex(clientHolder.getIndexName());
			builder.setType(type);
			bulk.add(builder.request());
		    }
		    bulk.get();
		}
	}

	protected Client getClient() {
		if (client == null) {
			ClientConfig clientConfig = new ClientConfig();
			client = JerseyClientBuilder.createClient(clientConfig);
		}
		return client;
	}

	protected Cookie login(String login, String password) {

		LoginPassword loginPassword = new LoginPassword();
		loginPassword.setLogin(login);
		loginPassword.setPasswordBase64(password);
		Entity<LoginPassword> entity = Entity.entity(loginPassword, MediaType.APPLICATION_JSON);
		Invocation.Builder builder = path("/auth/login").request();
		Response response = builder.post(entity, Response.class);
		return response.getCookies().get("JSESSIONID");
	}

	protected void logout(Cookie cookie) {
		path("/auth/logout").request().cookie(cookie).get();
	}

	protected WebTarget path(String path) {
		return getClient().target(BASE_URL).path(path);
	}

	protected static void destroyClient() {
		if (client != null) {
			client.close();
			client = null;
		}
	}

	@AfterClass
	public static void shutDown() throws Exception {
		if (server != null) {
			server.stop();
			server = null;
		}
	}
}
