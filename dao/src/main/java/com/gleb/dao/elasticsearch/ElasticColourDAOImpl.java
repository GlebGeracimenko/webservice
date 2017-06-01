package com.gleb.dao.elasticsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.gleb.dao.ColourDAO;
import com.gleb.dao.objects.DBColour;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by gleb on 16.09.15.
 */
@Repository
public class ElasticColourDAOImpl implements ColourDAO {

    private static Logger logger = LoggerFactory.getLogger(ElasticColourDAOImpl.class);
    public static final String COLOUR_TYPE = "colours";

    @Autowired
    private ElasticsearchClient clientHolder;

    private ObjectMapper jsonMapper;

    @PostConstruct
    private void init() {
        jsonMapper = new ObjectMapper();
    }

    @Override
    public String save(DBColour dbColor) {
        if (dbColor.getId() == null) {
            throw new RuntimeException("Please set ID for the Colour");
        }
        try {
            String source = jsonMapper.writeValueAsString(dbColor);

            clientHolder.getClient().prepareIndex(clientHolder.getIndexName(), COLOUR_TYPE, "" + dbColor.getId()).setSource(source).execute().actionGet();
            return dbColor.getId();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error("Can't save item!", e);
            return null;
        }
    }

    @Override
    public DBColour getById(String id) {
        GetResponse response = clientHolder.getClient().prepareGet(clientHolder.getIndexName(), COLOUR_TYPE, "" + id).execute().actionGet();
        String color = response.getSourceAsString();
        if (color == null) {

            SearchRequestBuilder requestBuilder = clientHolder.getClient().prepareSearch(clientHolder.getIndexName()).setTypes(COLOUR_TYPE)
                    .setSearchType(SearchType.DFS_QUERY_AND_FETCH).setQuery(QueryBuilders.matchQuery("id", id));
            SearchResponse response1 = requestBuilder.setExplain(false).execute().actionGet();
            if (response1.getSuccessfulShards() > 0) {
                SearchHit[] hits = response1.getHits().getHits();
                if (hits == null || hits.length < 1) {
                    logger.info("Can't find store by 'id' = " + id);
                    return null;
                }
                String source = hits[0].getSourceAsString();
                try {
                    return jsonMapper.readValue(source, DBColour.class);
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("Can't get item!", e);
                }
            }
        }
        try {
            return jsonMapper.readValue(color, DBColour.class);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Can't get item!", e);
        }
        return null;
    }

    @Override
    public DBColour getByName(String colourName) {
        SearchRequestBuilder requestBuilder = clientHolder.getClient().prepareSearch(clientHolder.getIndexName()).setTypes(COLOUR_TYPE)
                .setSearchType(SearchType.DFS_QUERY_AND_FETCH).setQuery(QueryBuilders.matchQuery("name", colourName));
        SearchResponse response = requestBuilder.setExplain(false).execute().actionGet();
        if (response.getSuccessfulShards() > 0) {
            SearchHit[] hits = response.getHits().getHits();
            if (hits == null || hits.length < 1) {
                return null;
            }
            String source = hits[0].getSourceAsString();
            try {
                return jsonMapper.readValue(source, DBColour.class);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("Can't get item!", e);
            }
        } else {
            throw new RuntimeException("Elasticsearcg communication error");
        }
        return null;
    }

    @Override
    public List<DBColour> getAll() {
        SearchRequestBuilder requestBuilder = clientHolder.getClient().prepareSearch(clientHolder.getIndexName()).setTypes(COLOUR_TYPE)
                .setSearchType(SearchType.DFS_QUERY_AND_FETCH).setQuery(QueryBuilders.matchAllQuery());
        SearchResponse response = requestBuilder.setExplain(false).execute().actionGet();
        List<DBColour> colours = new ArrayList<DBColour>();
        if (response.getSuccessfulShards() > 0) {
            for (SearchHit hit : response.getHits().getHits()) {
                String source = hit.getSourceAsString();
                try {
                    DBColour dbColour = jsonMapper.readValue(source, DBColour.class);
                    colours.add(dbColour);
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("Can't get item!", e);
                }
            }
        }
        return colours;
    }
}
