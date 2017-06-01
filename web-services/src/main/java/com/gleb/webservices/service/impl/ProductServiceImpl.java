package com.gleb.webservices.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.gleb.webservices.bo.BOColour;
import com.gleb.webservices.service.BrandsService;
import com.gleb.webservices.service.ColourService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.gleb.dao.ProductDAO;
import com.gleb.dao.WishlistDAO;
import com.gleb.dao.objects.DBProduct;
import com.gleb.dao.objects.DBProductListResponse;
import com.gleb.dao.objects.DBUser;
import com.gleb.dao.objects.DBWishlist;
import com.gleb.dao.utils.SearchQueryBuilder;
import com.gleb.webservices.bo.BOBrandWithFollowing;
import com.gleb.webservices.bo.BOProduct;
import com.gleb.webservices.bo.BOProductListResponse;
import com.gleb.webservices.mapping.ProductMapper;
import com.gleb.webservices.rest.Filters;
import com.gleb.webservices.service.IdsService;
import com.gleb.webservices.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    private static Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Value("${services.product.pagesize}")
    private Integer pagesize;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private IdsService idsService;

    @Autowired
    private BrandsService brandsService;

    @Autowired
    private ColourService colourService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private WishlistDAO wishlistDAO;

    @Override
    public BOProductListResponse getRecommendations(DBUser dbUser) {
        return new BOProductListResponse();
    }

    @Override
    public BOProduct getProduct(String id) {
        try {
            DBProduct product = productDAO.getById(id);
            return productMapper.map(product);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String saveProduct(BOProduct product) {
        DBProduct elDBProduct = productMapper.map(product);
        if (elDBProduct == null) {
            throw new RuntimeException("wrong product mapping");
        }
        if (elDBProduct.getId() == null) {
            String id = idsService.getNewIdAsString();
            elDBProduct.setId(id);
        }
        try {
            productDAO.saveProduct(elDBProduct);
            product.setId(elDBProduct.getId());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return product.getId();
    }

    @Override
    public BOProductListResponse getProducts(Collection<String> ids, UUID userId) {
        BOProductListResponse result = new BOProductListResponse();
        if (ids.isEmpty()) {
            return result;
        }
        Set<String> idsString = new HashSet<String>();
        for (String id : ids) {
            idsString.add(id);
        }
        try {
            Set<DBProduct> dbProducts = productDAO.getByIds(idsString);
            Set<BOProduct> boProducts = productMapper.map(dbProducts);
            Set<String> brandIds = new HashSet<String>();
            for (BOProduct boProduct : boProducts) {
                brandIds.add(boProduct.getBrand());
            }
            Collection<BOBrandWithFollowing> brands = brandsService.getBrands(brandIds, userId);
            result.addProducts(boProducts);
            result.addBrands(brands);
            result.setCount(boProducts.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public BOProductListResponse searchProducts(String searchText, DBUser dbUser, int from) {
        return searchProducts(searchText, dbUser, from, pagesize);
    }

    @Override
    public BOProductListResponse searchProducts(String searchText, DBUser dbUser, int from, int limit) {
        DBWishlist wishlist = wishlistDAO.getWishlist(dbUser.getId());
        Set<String> idsToSkip = new HashSet<String>();
        if (wishlist != null) {
            if (wishlist.getLikedItems() != null) {
                for (String likedItem : wishlist.getLikedItems()) {
                    idsToSkip.add(likedItem);
                }
            }
            if (wishlist.getDislikedItems() != null) {
                for (String dislikedItem : wishlist.getDislikedItems()) {
                    idsToSkip.add(dislikedItem);
                }
            }
        }
        SearchQueryBuilder queryBuilder = new SearchQueryBuilder();
        queryBuilder.setPhrase(searchText);
        queryBuilder.setGender(dbUser.getGender());
        queryBuilder.addExcludedProductIds(idsToSkip);
        try {
            DBProductListResponse response = productDAO.searchProducts(queryBuilder, from, limit);
            BOProductListResponse boResponse = new BOProductListResponse();
            boResponse.addProducts(productMapper.map(response.getProducts()));
            Set<BOBrandWithFollowing> boBrands = brandsService.getBrands(response.getBrandIds(), dbUser.getId());
            boResponse.addBrands(boBrands);
            boResponse.setCount(boResponse.getProducts().size() + from);
            return boResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new BOProductListResponse();
    }

    @Override
    public Set<BOProduct> getProductsByTopCategoryStyleAndPrice(String topCategory, String style, ArrayList<Integer> priceClusters, int from) {
        try {
            SearchQueryBuilder queryBuilder = new SearchQueryBuilder();
            if (priceClusters != null)
                queryBuilder.addClusters(priceClusters);
            if (style != null)
                queryBuilder.addStyles(Arrays.asList(style));
            if (topCategory != null)
                queryBuilder.addTopCategories(Arrays.asList(topCategory));
            DBProductListResponse response = productDAO.searchProducts(queryBuilder, from, pagesize);
            return productMapper.map(response.getProducts());
        } catch (JsonParseException e) {
            logger.error("can't find products: ", e.getMessage());
        } catch (JsonMappingException e) {
            logger.error("can't find products: ", e.getMessage());
        } catch (IOException e) {
            logger.error("can't find products: ", e.getMessage());
        }
        return new HashSet<BOProduct>();
    }

    @Override
    public Set<BOProduct> getProductsByPriceRecommendation(String topCategory, ArrayList<Integer> priceClusters, int from) {
        try {
            SearchQueryBuilder queryBuilder = new SearchQueryBuilder();
            queryBuilder.addClusters(priceClusters);
            DBProductListResponse response = productDAO.searchProducts(queryBuilder, from, pagesize);
            return productMapper.map(response.getProducts());
        } catch (JsonParseException e) {
            logger.error("can't find products: ", e.getMessage());
        } catch (JsonMappingException e) {
            logger.error("can't find products: ", e.getMessage());
        } catch (IOException e) {
            logger.error("can't find products: ", e.getMessage());
        }
        return new HashSet<BOProduct>();
    }

    @Override
    public Set<BOProduct> getProductsByTopCategory(String topCategory, String searchText, DBUser dbUser, int from) {
        try {
            SearchQueryBuilder queryBuilder = new SearchQueryBuilder();
            if (topCategory != null)
                queryBuilder.addTopCategories(Arrays.asList(topCategory));
            queryBuilder.setGender(dbUser.getGender());
            queryBuilder.setPhrase(searchText);
            DBProductListResponse response = productDAO.searchProducts(queryBuilder, from, pagesize);
            return productMapper.map(response.getProducts());
        } catch (JsonParseException e) {
            logger.error("can't find products: ", e.getMessage());
        } catch (JsonMappingException e) {
            logger.error("can't find products: ", e.getMessage());
        } catch (IOException e) {
            logger.error("can't find products: ", e.getMessage());
        }
        return new HashSet<BOProduct>();
    }

    @Override
    public Set<BOProduct> getProductsByTopCategoryAndColours(String topCategory, List<String> colours, DBUser dbUser, int from) {
        List<String> colourNames = new ArrayList<>();
        for (String colourId : colours) {
            BOColour colour = colourService.getColourById(colourId);
            colourNames.add(colour.getName());
        }
        SearchQueryBuilder builder = new SearchQueryBuilder();
        builder.setGender(dbUser.getGender());
        if (topCategory != null)
            builder.addTopCategories(Arrays.asList(topCategory));
        if (colourNames != null)
            builder.addColours(colourNames);
        try {
            DBProductListResponse response = productDAO.searchProducts(builder, from, 50);
            return productMapper.map(response.getProducts());
        } catch (JsonParseException e) {
            logger.warn("Error during search by top category and colours. ", e);
        } catch (JsonMappingException e) {
            logger.warn("Error during search by top category and colours. ", e);
        } catch (IOException e) {
            logger.warn("Error during search by top category and colours. ", e);
        }
        return new HashSet<BOProduct>();
    }

    @Override
    public BOProductListResponse searchProductsByFilter(Filters filters, DBUser dbUser, int from) {
        SearchQueryBuilder builder = new SearchQueryBuilder().addBrandIds(filters.getBrands()).addColours(filters.getColours())
                .addTopCategories(filters.getTopCategory()).setGender(filters.getGender()).setPhrase(filters.getPhrase()).addStyles(filters.getStyle())
                .addClusters(filters.getCluster()).addSizes(filters.getSize());
        if (filters.getPriceFrom() != null)
            builder.addPriceFrom(filters.getPriceFrom());
        if (filters.getPriceTo() != null)
            builder.addPriceTo(filters.getPriceTo());
        BOProductListResponse boResponse = new BOProductListResponse();
        try {
            DBProductListResponse dbResponse = productDAO.searchProducts(builder, from, pagesize);
            boResponse.addProducts(productMapper.map(dbResponse.getProducts()));
            Set<BOBrandWithFollowing> boBrands = brandsService.getBrands(dbResponse.getBrandIds(), dbUser.getId());
            boResponse.addBrands(boBrands);
            boResponse.setCount(boResponse.getProducts().size() + from);
        } catch (JsonParseException e) {
            logger.warn("Error during search by filters. ", e);
        } catch (JsonMappingException e) {
            logger.warn("Error during search by filters. ", e);
        } catch (IOException e) {
            logger.warn("Error during search by filters. ", e);
        }
        return boResponse;
    }

    @Override
    public void deleteProduct(String id) {
        try {
            productDAO.deleteProduct(id);
        } catch (IOException e) {
            logger.warn("Can't delete product id:"+id+". reason:", e);
        }
    }
}