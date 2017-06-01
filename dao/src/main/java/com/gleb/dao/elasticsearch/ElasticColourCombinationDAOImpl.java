package com.gleb.dao.elasticsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.gleb.dao.ColourCombinationsDAO;
import com.gleb.dao.objects.DBColourCombination;
import org.elasticsearch.action.delete.DeleteAction;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
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

@Repository
public class ElasticColourCombinationDAOImpl implements ColourCombinationsDAO {

    private static Logger logger = LoggerFactory.getLogger(ElasticColourCombinationDAOImpl.class);

    @Autowired
    private ElasticsearchClient clientHolder;

    private ObjectMapper jsonMapper;

    @PostConstruct
    private void init() {
        jsonMapper = new ObjectMapper();
    }

    @Override
    public void save(DBColourCombination dbColourCombination) {
        if (dbColourCombination.getId() == null) {
            throw new RuntimeException("Please set ID for the Colour Combination");
        }
        try {
            String source = jsonMapper.writeValueAsString(dbColourCombination);
            clientHolder.getClient().prepareIndex(clientHolder.getIndexName(), COLOUR_COMBINATIONS_TYPE, "" + dbColourCombination.getId()).setSource(source)
                    .execute().actionGet();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error("Can't save item!", e);
        }
    }

    @Override
    public void delete(DBColourCombination dbColourCombination) {
        String id = dbColourCombination.getId().toString();
        DeleteRequestBuilder builder = new DeleteRequestBuilder(clientHolder.getClient(), DeleteAction.INSTANCE);
        builder.setId(id);
        builder.setType(COLOUR_COMBINATIONS_TYPE);
        builder.setIndex(clientHolder.getIndexName());
        builder.get();
    }

    @Override
    public List<DBColourCombination> getAll() {
        SearchRequestBuilder requestBuilder = clientHolder.getClient().prepareSearch(clientHolder.getIndexName()).setTypes(COLOUR_COMBINATIONS_TYPE)
                .setSearchType(SearchType.DFS_QUERY_AND_FETCH).setQuery(QueryBuilders.matchAllQuery());
        SearchResponse response = requestBuilder.setExplain(false).execute().actionGet();
        List<DBColourCombination> dbColourCombinations = new ArrayList<DBColourCombination>();
        if (response.getSuccessfulShards() > 0) {
            SearchHit[] hits = response.getHits().getHits();
            if (hits == null || hits.length < 1) {
                return null;
            }
            try {
                for (SearchHit hit : hits) {
                    String source = hit.getSourceAsString();
                    DBColourCombination colourCombination = jsonMapper.readValue(source, DBColourCombination.class);
                    dbColourCombinations.add(colourCombination);
                }
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("Elasticsearch communication error");
        }
        return dbColourCombinations;
    }

    @Override
    public List<DBColourCombination> getByColour(String colourGroupName) {
        SearchRequestBuilder requestBuilder = clientHolder.getClient().prepareSearch(clientHolder.getIndexName()).setTypes(COLOUR_COMBINATIONS_TYPE)
                .setSearchType(SearchType.DFS_QUERY_AND_FETCH).setQuery(QueryBuilders.matchQuery("mainColourName", colourGroupName));
        SearchResponse response = requestBuilder.setExplain(false).execute().actionGet();
        List<DBColourCombination> dbColourCombinations = new ArrayList<DBColourCombination>();
        if (response.getSuccessfulShards() > 0) {
            SearchHit[] hits = response.getHits().getHits();
            DBColourCombination colourCombination = new DBColourCombination();
            if (hits == null || hits.length < 1) {
                return null;
            }
            try {
                for (SearchHit hit : hits) {
                    String source = hit.getSourceAsString();
                    colourCombination = jsonMapper.readValue(source, DBColourCombination.class);
                    dbColourCombinations.add(colourCombination);
                }
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("Elasticsearch communication error");
        }
        return dbColourCombinations;
    }

    @Override
    public DBColourCombination getByColourAndType(String combinationName, String colourGroupName) {
        BoolQueryBuilder rootQuery = QueryBuilders.boolQuery();
        /**
         * 'Must' queries
         */
        rootQuery.must(QueryBuilders.termQuery("mainColourName", colourGroupName));
        rootQuery.must(QueryBuilders.termsQuery("combinationName", combinationName));

        SearchRequestBuilder requestBuilder = clientHolder.getClient().prepareSearch(clientHolder.getIndexName()).setTypes(COLOUR_COMBINATIONS_TYPE)
                .setSearchType(SearchType.DFS_QUERY_AND_FETCH).setQuery(rootQuery);
        SearchResponse response = requestBuilder.setExplain(false).execute().actionGet();
        if (response.getSuccessfulShards() > 0) {
            SearchHit[] hits = response.getHits().getHits();
            if (hits == null || hits.length < 1) {
                return null;
            }
            try {
                String source = hits[0].getSourceAsString();
                DBColourCombination colourCombination = jsonMapper.readValue(source, DBColourCombination.class);
                return colourCombination;
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("Elasticsearch communication error");
        }
        return null;
    }
}