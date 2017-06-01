package com.gleb.dao.elasticsearch;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.notQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import com.gleb.dao.BrandsDAO;
import com.gleb.dao.ColourDAO;
import com.gleb.dao.ProductDAO;
import com.gleb.dao.objects.DBProduct;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gleb.dao.objects.DBProductListResponse;
import com.gleb.dao.utils.SearchQueryBuilder;

@Repository
public class ElasticProductDAOImpl implements ProductDAO {

    @Autowired
    private BrandsDAO brandsDAO;

    @Autowired
    private ColourDAO colourDAO;

    @Autowired
    private ElasticsearchClient clientHolder;

    private ObjectMapper jsonMapper;

    @PostConstruct
    public void init() {
        jsonMapper = new ObjectMapper();
    }

    public DBProduct getById(String id) throws JsonParseException, JsonMappingException, IOException {
        GetResponse response = clientHolder.getClient().prepareGet(clientHolder.getIndexName(), PRODUCT_TYPE, "" + id).execute().actionGet();
        String product = response.getSourceAsString();
        if (product == null) {
            return null;
        }
        return jsonMapper.readValue(product, DBProduct.class);
    }

    @Override
    public DBProduct getByImportedId(String importedId) {
        SearchRequestBuilder requestBuilder = clientHolder.getClient().prepareSearch(clientHolder.getIndexName()).setTypes(PRODUCT_TYPE)
                .setSearchType(SearchType.DFS_QUERY_AND_FETCH).setQuery(QueryBuilders.matchQuery("importedId", importedId));
        SearchResponse response = requestBuilder.setExplain(true).execute().actionGet();
        if (response.getSuccessfulShards() > 0) {
            SearchHit[] hits = response.getHits().hits();
            if (hits != null && hits.length > 0) {
                String source = response.getHits().getHits()[0].getSourceAsString();
                try {
                    return jsonMapper.readValue(source, DBProduct.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public List<DBProduct> findByFilter(Map<String, Object> filters) {
        return null;
    }

    @Override
    public List<DBProduct> findForGender(int gender, int count) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<DBProduct> getBySkus(List<String> skus) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<DBProduct> getByIds(Collection<String> ids) throws JsonParseException, JsonMappingException, IOException {
        MultiGetRequestBuilder builder = clientHolder.getClient().prepareMultiGet().add(clientHolder.getIndexName(), PRODUCT_TYPE, ids);
        MultiGetResponse response = builder.get();

        Set<DBProduct> result = new HashSet<DBProduct>();
        for (MultiGetItemResponse hit : response) {
            if (hit.getResponse().isSourceEmpty()) {
                continue;
            }
            String source = new String(hit.getResponse().getSourceAsBytes());
            DBProduct product = jsonMapper.readValue(source, DBProduct.class);
            result.add(product);
        }
        return result;
    }

    @Override
    public String saveProduct(DBProduct product) throws JsonProcessingException {
        if (product.getId() == null) {
            throw new RuntimeException("Please set ID for the Product");
        }
        String source = jsonMapper.writeValueAsString(product);
        IndexResponse response = clientHolder.getClient().prepareIndex(clientHolder.getIndexName(), PRODUCT_TYPE, "" + product.getId()).setSource(source)
                .execute().actionGet();
        return response.getId();
    }

    /**
     * Search for products by phrase, gender, etc, but also with 'must' filter by fields;
     * 
     * @param mustFields
     *            - Mapped field name with values that must be in found objects.
     * @param phrase
     *            - Phrase for search. empty means 'all'
     * @param gender
     *            - user's gender
     * @param followingBrands
     *            - all user's following brands
     * @param idsToSkip
     *            - which ids we should skip (liked for example)
     * @param from
     *            - starting from index
     * @param size
     *            - pagesize
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */

    @Override
    public DBProductListResponse searchProducts(SearchQueryBuilder builder, int from, int size) throws JsonParseException, JsonMappingException, IOException {
        DBProductListResponse result = new DBProductListResponse();

        TermsBuilder brandsAggregation = AggregationBuilders.terms(BRANDS_AGGREGATION_NAME).field("brand");
        BoolQueryBuilder boolQuery = boolQuery();

        /**
         * matchAll flag shows, that we need to add only 'matchAll' filter. That's for the case when we haven't any strict params.
         * 
         * First - let's check fields for brands and product ids.
         * 
         * Brands:
         */
        boolean matchAll = true;
        if (!builder.getBrandIds().isEmpty()) {
            boolQuery.must(termsQuery("brand", builder.getBrandIds()));
            matchAll = false;
        }

        /**
         * brands to exclude:
         */

        if (!builder.getBrandIdsExclude().isEmpty()) {
            boolQuery.must(notQuery(termsQuery("brand", builder.getBrandIdsExclude())));
            matchAll = false;
        }

        /**
         * products:
         */

        if (!builder.getProductIds().isEmpty()) {
            boolQuery.must(termsQuery("_id", builder.getProductIds()));
            matchAll = false;
        }

        /**
         * products to exclude:
         */

        if (!builder.getProductIdsExclude().isEmpty()) {
            boolQuery.mustNot(termsQuery("_id", builder.getProductIdsExclude()));
            matchAll = false;
        }

        /**
         * top categories
         * 
         * 'match' query applies the same standard analyzer to the search term which used for field when stored document to the index. String-fields indexed as 'analyzed' when
         * mapping a document (Upper case changes to lower, for example)
         * 
         */

        if (!builder.getTopCategories().isEmpty()) {
            boolQuery.must(matchQuery("topCategory", builder.getTopCategories()));
            matchAll = false;
        }

        /**
         * colours
         */

        if (!builder.getColours().isEmpty()) {
            boolQuery.must(termsQuery("color", builder.getColours()));
            matchAll = false;
        }

        /**
         * clusters
         */

        if (!builder.getClusters().isEmpty()) {
            boolQuery.must(termsQuery("cluster", builder.getClusters()));
            matchAll = false;
        }

        /**
         * styles
         */

        if (!builder.getStyles().isEmpty()) {
            boolQuery.must(matchQuery("style", builder.getStyles()));
            matchAll = false;
        }

        /**
         * sizes
         */

        if (!builder.getSizes().isEmpty()) {
            boolQuery.must(termsQuery("size", builder.getSizes()));
            matchAll = false;
        }

        /**
         * 'Range' queries
         */
        /**
         * price_from
         */

        if (builder.getPriceFrom() != null) {
            boolQuery.must(rangeQuery("price").from(builder.getPriceFrom()));
            matchAll = false;
        }

        /**
         * price_to
         */

        if (builder.getPriceTo() != null) {
            boolQuery.must(rangeQuery("price").to(builder.getPriceTo()));
            matchAll = false;
        }

        if (matchAll) {
            boolQuery.must(matchAllQuery());
        }

        /**
         * 'Must' queries
         */
        boolQuery.must(termQuery("gender", builder.getGender()));

        /**
         * 'Should' queries Operator value is used for 'few words' search case. When user specifies few words in phrase - we will search only products contain all of them instead
         * of 'aby of them'
         */

        if (builder.getPhrase() == null || builder.getPhrase().trim().equals("")) {
            if (matchAll) {
                boolQuery.minimumNumberShouldMatch(1);
                boolQuery.should(matchAllQuery());
            }
        } else {
            boolQuery.minimumNumberShouldMatch(1);
            boolQuery.should(matchQuery("name", builder.getPhrase()).operator(Operator.AND));
            boolQuery.should(matchQuery("style", builder.getPhrase()).operator(Operator.AND));
            boolQuery.should(matchQuery("category", builder.getPhrase()).operator(Operator.AND));
            boolQuery.should(matchQuery("desc", builder.getPhrase()).operator(Operator.AND));
        }

        SearchRequestBuilder requestBuilder = clientHolder.getClient().prepareSearch(clientHolder.getIndexName()).setTypes(PRODUCT_TYPE)
                .setSearchType(SearchType.DFS_QUERY_AND_FETCH).addAggregation(brandsAggregation).setQuery(boolQuery).setFrom(from).setSize(size);

        SearchResponse response = requestBuilder.setExplain(true).get();
        Aggregations aggs = response.getAggregations();
        Terms agg = aggs.get(BRANDS_AGGREGATION_NAME);
        for (Bucket bucket : agg.getBuckets()) {
            String brandId = bucket.getKeyAsString();
            result.addBrandId(brandId);
        }
        SearchHits hits = response.getHits();
        for (SearchHit hit : hits) {
            String source = hit.getSourceAsString();
            DBProduct product = jsonMapper.readValue(source, DBProduct.class);
            result.addProduct(product);
        }
        return result;
    }

    @Override
    public void deleteProduct(String id) throws JsonParseException, JsonMappingException, IOException {
        DeleteRequestBuilder requestBuilder = clientHolder.getClient().prepareDelete(clientHolder.getIndexName(), PRODUCT_TYPE, id);
        requestBuilder.get();
    }
}