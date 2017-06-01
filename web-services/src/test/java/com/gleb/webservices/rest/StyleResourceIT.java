package com.gleb.webservices.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.gleb.webservices.bo.BOStyle;
import com.gleb.webservices.test.fixtures.UserFixture;
import com.gleb.webservices.tests.BasicIntegrationTest;
import com.gleb.dao.FollowsDAO;
import com.gleb.dao.StylesDAO;
import com.gleb.dao.UsersDAO;
import com.gleb.dao.cassandra.BasicCQLCassandraDAO;
import com.gleb.dao.cassandra.CQLUserDAO;
import com.gleb.dao.elasticsearch.ElasticStylesDAOImpl;
import com.gleb.dao.elasticsearch.ElasticsearchClient;
import com.gleb.dao.objects.DBUser;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteAction;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created by gleb on 30.11.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration("/testApplicationContext.xml") })
public class StyleResourceIT extends BasicIntegrationTest {

    @Autowired
    private StylesDAO stylesDAO;

    @Autowired
    private UsersDAO usersDAO;

    @Autowired
    private BasicCQLCassandraDAO basicCQLDAO;

    @Autowired
    private ElasticsearchClient clientHolder;

    @Autowired
    private FollowsDAO followsDAO;

    private List<BOStyle> styleList = new ArrayList<BOStyle>();

    private DBUser testUser = UserFixture.createUserImporter(UUID.randomUUID());

    private void cleanUp() {
        basicCQLDAO.getSession().execute("truncate " + CQLUserDAO.USERS_COLUMN_FAMILY);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SearchRequestBuilder requestBuilder = clientHolder.getClient().prepareSearch(clientHolder.getIndexName()).setTypes(ElasticStylesDAOImpl.STYLES_TYPE)
                .setSearchType(SearchType.DFS_QUERY_AND_FETCH).setQuery(QueryBuilders.matchAllQuery()).setNoFields();
        SearchResponse response = requestBuilder.setExplain(true).execute().actionGet();
        SearchHits hits = response.getHits();
        if (hits.getTotalHits() > 0) {
            BulkRequestBuilder bulk = clientHolder.getClient().prepareBulk();
            for (SearchHit hit : hits) {
                DeleteRequestBuilder builder = new DeleteRequestBuilder(clientHolder.getClient(), DeleteAction.INSTANCE);
                builder.setId(hit.getId());
                builder.setIndex(clientHolder.getIndexName());
                builder.setType(ElasticStylesDAOImpl.STYLES_TYPE);
                bulk.add(builder.request());
            }
            bulk.get();
        }
    }

    @After
    public void after() {
        cleanUp();
    }

    @Before
    public void before() {
        cleanUp();
        usersDAO.saveUser(testUser);
    }

    private BOStyle create(String id, String value) {
        BOStyle style = new BOStyle();
        style.setId(id);
        style.setName(value);
        style.setDescription(value + "_description");
        return style;
    }

    private void addAll() {
        styleList.add(create("1L", "Test1"));
        styleList.add(create("2L", "Test2"));
        styleList.add(create("3L", "Test3"));
        styleList.add(create("4L", "Test4"));
        styleList.add(create("5L", "Test5"));
    }

    private void check(BOStyle style1, BOStyle style2) {
        assertThat(style1.getId(), equalTo(style2.getId()));
        assertThat(style1.getName(), equalTo(style2.getName()));
        assertThat(style1.getDescription(), equalTo(style2.getDescription()));
    }

    private void save() {
        Cookie cookie = login(testUser.getLogin(), testUser.getPassword());
        for (BOStyle style : styleList) {
            Entity<BOStyle> entity = Entity.entity(style, MediaType.APPLICATION_JSON);
            Response response = path("/styles").request().cookie("JSESSIONID", cookie.getValue()).post(entity);
            assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
        }
        logout(cookie);
    }

    private void getById() {
        Cookie cookie = login(testUser.getLogin(), testUser.getPassword());
        for (BOStyle style : styleList) {
            BOStyle boStyle = path("/styles/" + style.getId()).request().cookie("JSESSIONID", cookie.getValue()).get(BOStyle.class);
            check(style, boStyle);
        }
        logout(cookie);
    }

    private void getAllStyles() {
        Cookie cookie = login(testUser.getLogin(), testUser.getPassword());
        Collection<BOStyle> boStyles = path("/styles").request().cookie("JSESSIONID", cookie.getValue()).get(new GenericType<Collection<BOStyle>>(){});
        int count = 0;
        for (BOStyle boStyle : boStyles) {
            if ((count = styleList.indexOf(boStyle)) > -1) {
                check(boStyle, styleList.get(count));
            }
        }
        logout(cookie);
    }

    private void follow() {
        Cookie cookie = login(testUser.getLogin(), testUser.getPassword());
        List<String> longList = new ArrayList<String>();
        for (BOStyle style : styleList) {
            Response response = path("/styles/follow/" + style.getId()).request().cookie(cookie).post(null);
            assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
            longList.add(style.getId());
        }
        Collection<String> followsIds = followsDAO.getAllFollowedObjectsByUser(testUser.getId(), FollowsDAO.TYPE_STYLE);
        assertThat(followsIds.size(), equalTo(styleList.size()));
        for (String aLong : followsIds) {
            assertThat(aLong, equalTo(longList.get(longList.indexOf(aLong))));
        }
        logout(cookie);
    }

    private void followingStyles() {
        Cookie cookie = login(testUser.getLogin(), testUser.getPassword());
        Collection<BOStyle> boStyles = path("/styles/following").request().cookie("JSESSIONID", cookie.getValue()).get(new GenericType<Collection<BOStyle>>(){});
        assertNotNull(boStyles);
        assertThat(boStyles.size(), equalTo(styleList.size()));
        int count = 0;
        for (BOStyle boStyle : boStyles) {
            if ((count = styleList.indexOf(boStyle)) > -1) {
                check(boStyle, styleList.get(count));
            }
        }
    }

    private void unfollow() {
        Cookie cookie = login(testUser.getLogin(), testUser.getPassword());
        for (BOStyle style : styleList) {
            Response response = path("/styles/unfollow/" + style.getId()).request().cookie("JSESSIONID", cookie.getValue()).post(null);
            assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
        }
        Collection<String> followsIds = followsDAO.getAllFollowedObjectsByUser(testUser.getId(), FollowsDAO.TYPE_STYLE);
        assertThat(followsIds.size(), equalTo(0));
        logout(cookie);
    }

    @Test
    public void test() {
        addAll();
        save();
        getById();
        getAllStyles();
        follow();
        followingStyles();
        unfollow();
    }

}
