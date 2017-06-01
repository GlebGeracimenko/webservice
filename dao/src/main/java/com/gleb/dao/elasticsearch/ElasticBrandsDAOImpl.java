package com.gleb.dao.elasticsearch;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import com.gleb.dao.BrandsDAO;
import com.gleb.dao.objects.DBUser;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
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
import com.gleb.dao.objects.DBBrand;

@Repository
public class ElasticBrandsDAOImpl implements BrandsDAO {
    private static Logger logger = LoggerFactory.getLogger(ElasticBrandsDAOImpl.class);
    public static final String BRAND_TYPE = "brands";
    @Autowired
    private ElasticsearchClient clientHolder;

    private ObjectMapper jsonMapper;

    @PostConstruct
    public void init() {
        jsonMapper = new ObjectMapper();
    }

    @Override
    public String saveBrand(DBBrand dbBrand) {
        try {
            String source = jsonMapper.writeValueAsString(dbBrand);
            clientHolder.getClient().prepareIndex(clientHolder.getIndexName(), BRAND_TYPE, dbBrand.getId()).setSource(source).execute().actionGet();
            return dbBrand.getId();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error("Can't save item.", e);
            return null;
        }
    }

    @Override
    public DBBrand getBrand(String id) {
        GetResponse response = clientHolder.getClient().prepareGet(clientHolder.getIndexName(), BRAND_TYPE, id).execute().actionGet();
        String result = response.getSourceAsString();
        if (result == null) {
            // TODO log4j???
            return null;
        }
        try {
            return jsonMapper.readValue(result, DBBrand.class);
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
    public DBBrand getBrandByName(String name) {
        SearchRequestBuilder builder = clientHolder.getClient().prepareSearch(clientHolder.getIndexName()).setTypes(BRAND_TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(QueryBuilders.matchQuery("name", name));
        SearchResponse response = builder.setExplain(false).execute().actionGet();
        DBBrand dbBrand = null;
        SearchHit[] hits = response.getHits().getHits();
        if (hits != null && hits.length > 0) {
            String source = hits[0].getSourceAsString();
            try {
                dbBrand = jsonMapper.readValue(source, DBBrand.class);
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
        return dbBrand;
    }

    @Override
    public DBBrand getBrandByDescription(String descPhrase) {
        return null;
    }

    @Override
    public List<DBBrand> getAllBrands() {
        SearchRequestBuilder builder = clientHolder.getClient().prepareSearch(clientHolder.getIndexName()).setTypes(BRAND_TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        MatchAllQueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        builder = builder.setQuery(queryBuilder);
        SearchResponse response = builder.setExplain(true).execute().actionGet();

        List<DBBrand> result = new ArrayList<DBBrand>();
        try {
            for (SearchHit hit : response.getHits().hits()) {
                String source = hit.sourceAsString();
                DBBrand dbBrand = jsonMapper.readValue(source, DBBrand.class);
                result.add(dbBrand);
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
        return new ArrayList<DBBrand>();
    }

    @Override
    public List<DBBrand> getAllBrands(Map<String, Object> query, DBUser dbUser, int from, int size) {
        SearchRequestBuilder builder = clientHolder.getClient().prepareSearch(clientHolder.getIndexName()).setTypes(BRAND_TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQuery = null;
        if (!query.isEmpty()) {
            boolQuery = boolQuery();
            Iterator<String> keys = query.keySet().iterator();
            String currentKey = keys.next();

            while (keys.hasNext()) {
                currentKey = keys.next();
                boolQuery.must(matchQuery(currentKey, query.get(currentKey)));
            }
        }
        SearchResponse response = null;
        if (boolQuery != null) {
            response = builder.setFrom(from).setSize(size).setQuery(boolQuery).setExplain(true).execute().actionGet();
        } else {
            response = builder.setFrom(from).setSize(size).setExplain(true).execute().actionGet();
        }
        List<DBBrand> result = new ArrayList<DBBrand>(size);
        try {
            for (SearchHit hit : response.getHits().hits()) {
                String source = hit.sourceAsString();
                DBBrand dbBrand = jsonMapper.readValue(source, DBBrand.class);
                result.add(dbBrand);
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
        return new ArrayList<DBBrand>();
    }

    @Override
    public Collection<DBBrand> getBrands(Collection<String> brandIds) {
        if (brandIds == null || brandIds.isEmpty()) {
            return new ArrayList<DBBrand>();
        }
        MultiGetRequestBuilder builder = clientHolder.getClient().prepareMultiGet().add(clientHolder.getIndexName(), BRAND_TYPE, brandIds);
        MultiGetResponse response = builder.get();

        Set<DBBrand> result = new HashSet<DBBrand>();
        try {
            for (MultiGetItemResponse hit : response) {
                if (hit.getResponse().isSourceEmpty()) {
                    continue;
                }
                String source = new String(hit.getResponse().getSourceAsBytes());

                DBBrand brand = jsonMapper.readValue(source, DBBrand.class);
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
        return new ArrayList<DBBrand>();
    }
}
