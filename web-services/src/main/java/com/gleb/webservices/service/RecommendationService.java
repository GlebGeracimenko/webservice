package com.gleb.webservices.service;

import java.util.List;
import java.util.Set;

import com.gleb.webservices.bo.BOProductListResponse;
import com.gleb.dao.objects.DBUser;
import com.gleb.webservices.bo.BOProduct;

public interface RecommendationService {

  BOProductListResponse getRecomendations(DBUser dbuser);

  Set<BOProduct> getRecommendationsByTopCategoryAndColoursIds(List<String> colours, String topCategory, DBUser dbUser);

  Set<BOProduct> getPriceRecommendationsByTopCategoryAndStyle(String topCategory, String style, DBUser dbUser);

  Set<BOProduct> getPriceRecommendations(DBUser dbUser);
}
