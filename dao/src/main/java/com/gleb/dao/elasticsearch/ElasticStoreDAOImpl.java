package com.gleb.dao.elasticsearch;

import java.io.IOException;

import javax.annotation.PostConstruct;

import com.gleb.dao.objects.DBStore;
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
import com.gleb.dao.StoreDAO;

/**
 * Created by gleb on 14.09.15.
 */
@Repository
public class ElasticStoreDAOImpl implements StoreDAO {
    private static Logger logger = LoggerFactory.getLogger(ElasticStoreDAOImpl.class);
    public static final String STORE_TYPE = "stores";

    @Autowired
    private ElasticsearchClient clientHolder;

    private ObjectMapper jsonMapper;

    @PostConstruct
    private void init() {
        jsonMapper = new ObjectMapper();
    }

    @Override
    public String save(DBStore dbStore) {
        try {
            String source = jsonMapper.writeValueAsString(dbStore);
            clientHolder.getClient().prepareIndex(clientHolder.getIndexName(), STORE_TYPE, "" + dbStore.getId()).setSource(source).execute().actionGet();
            return dbStore.getId();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error("Can't save item!", e);
            return null;
        }
    }

    @Override
    public boolean update(DBStore dbStore) {
        try {
            String source = jsonMapper.writeValueAsString(dbStore);
            clientHolder.getClient().prepareUpdate(clientHolder.getIndexName(), STORE_TYPE, "" + dbStore.getId()).setDoc(source.getBytes()).get();
            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error("Can't update item!", e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Can't update item!", e);
        }
        return false;
    }

    @Override
    public DBStore getById(String id) {
        GetResponse response = clientHolder.getClient().prepareGet(clientHolder.getIndexName(), STORE_TYPE, "" + id).execute().actionGet();
        String product = response.getSourceAsString();
        if (product == null) {
            logger.info("Can't find store by 'id' = " + id);
            return null;
        }
        try {
            return jsonMapper.readValue(product, DBStore.class);
            /*???
            catch (JsonParseException e) {
            e.printStackTrace();
            logger.error("Can't get item.", e);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            logger.error("Can't get item.", e);
             ???*/
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Can't get item!", e);
        }
        return null;
    }

    @Override
    public DBStore getByName(String nameStore) {
        SearchRequestBuilder requestBuilder = clientHolder.getClient().prepareSearch(clientHolder.getIndexName()).setTypes(STORE_TYPE)
                .setSearchType(SearchType.DFS_QUERY_AND_FETCH).setQuery(QueryBuilders.matchQuery("name", nameStore));
        SearchResponse response = requestBuilder.setExplain(false).execute().actionGet();
        SearchHit[] hits = response.getHits().getHits();
        if (hits != null && hits.length > 0) {
            String source = hits[0].getSourceAsString();
            try {
                return jsonMapper.readValue(source, DBStore.class);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("Can't get item!", e);
            }
        }
        return null;
    }
}
