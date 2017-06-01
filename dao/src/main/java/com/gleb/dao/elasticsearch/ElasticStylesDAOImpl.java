package com.gleb.dao.elasticsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import com.gleb.dao.objects.DBStyle;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gleb.dao.StylesDAO;

@Repository
public class ElasticStylesDAOImpl implements StylesDAO {
    private static Logger logger = LoggerFactory.getLogger(ElasticStylesDAOImpl.class);
    public static final String STYLES_TYPE = "styles";

    @Autowired
    private ElasticsearchClient clientHolder;

    private ObjectMapper jsonMapper;

    @PostConstruct
    public void init() {
	jsonMapper = new ObjectMapper();
    }

    @Override
    public String saveStyle(DBStyle dbStyle) {
	try {
	    String source = jsonMapper.writeValueAsString(dbStyle);
	    clientHolder.getClient().prepareIndex(clientHolder.getIndexName(), STYLES_TYPE, "" + dbStyle.getId()).setSource(source).execute().actionGet();
	    return dbStyle.getId();
	} catch (JsonProcessingException e) {
	    e.printStackTrace();
	    logger.error("Can't save style.", e);
	    return null;
	}
    }

    @Override
    public DBStyle getStyleById(String id) {
	GetResponse response = clientHolder.getClient().prepareGet(clientHolder.getIndexName(), STYLES_TYPE, "" + id).execute().actionGet();
	String product = response.getSourceAsString();
	if (product == null) {
	    return null;
	}
	try {
	    return jsonMapper.readValue(product, DBStyle.class);
	} catch (JsonParseException e) {
	    e.printStackTrace();
	    logger.error("Can't get item.", e);
	} catch (JsonMappingException e) {
	    e.printStackTrace();
	    logger.error("Can't get item.", e);
	} catch (IOException e) {
	    e.printStackTrace();
	    logger.error("Can't get item.", e);
	}
	return null;
    }

    @Override
    public DBStyle getStyleByName(String name) {
	SearchRequestBuilder builder = clientHolder.getClient().prepareSearch(clientHolder.getIndexName()).setTypes(STYLES_TYPE).setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		.setQuery(QueryBuilders.matchQuery("name", name));
	SearchResponse response = builder.setExplain(false).execute().actionGet();
	DBStyle dbStyle = null;
		SearchHit[] hits = response.getHits().getHits();
		if (hits != null && hits.length > 0) {
			String source = hits[0].getSourceAsString();
	    try {
		dbStyle = jsonMapper.readValue(source, DBStyle.class);
	    } catch (JsonParseException e) {
		e.printStackTrace();
		logger.error("Can't get item.", e);
	    } catch (JsonMappingException e) {
		e.printStackTrace();
		logger.error("Can't get item.", e);
	    } catch (IOException e) {
		e.printStackTrace();
		logger.error("Can't get item.", e);
	    }
	}
	return dbStyle;
    }

    @Override
    public List<DBStyle> getAllStyles() {
	SearchRequestBuilder builder = clientHolder.getClient().prepareSearch(clientHolder.getIndexName()).setTypes(STYLES_TYPE).setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
	SearchResponse response = builder.setQuery(QueryBuilders.matchAllQuery()).setExplain(true).execute().actionGet();

	List<DBStyle> result = new ArrayList<DBStyle>();
	try {
	    for (SearchHit hit : response.getHits().hits()) {
		String source = hit.sourceAsString();
		DBStyle dbStyle = jsonMapper.readValue(source, DBStyle.class);
		result.add(dbStyle);
	    }
	    return result;
	} catch (JsonParseException e) {
	    e.printStackTrace();
	    logger.error("Can't map item.", e);
	} catch (JsonMappingException e) {
	    e.printStackTrace();
	    logger.error("Can't map item.", e);
	} catch (IOException e) {
	    e.printStackTrace();
	    logger.error("Can't map item.", e);
	}
	return new ArrayList<DBStyle>();
    }

    public Collection<DBStyle> getStyles(Collection<String> styleIds) {
	MultiGetRequestBuilder builder = clientHolder.getClient().prepareMultiGet().add(clientHolder.getIndexName(), STYLES_TYPE, styleIds);
	MultiGetResponse response = builder.get();

	Set<DBStyle> result = new HashSet<DBStyle>();
	try {
	    for (MultiGetItemResponse hit : response) {
		String source = new String(hit.getResponse().getSourceAsBytes());
		DBStyle brand = jsonMapper.readValue(source, DBStyle.class);
		result.add(brand);
	    }
	    return result;
	} catch (JsonParseException e) {
	    e.printStackTrace();
	    logger.error("Can'tq map brand.", e);
	} catch (JsonMappingException e) {
	    e.printStackTrace();
	    logger.error("Can't map brand.", e);
	} catch (IOException e) {
	    e.printStackTrace();
	    logger.error("Can't map brand.", e);
	}
	return new ArrayList<DBStyle>();
    }
}
