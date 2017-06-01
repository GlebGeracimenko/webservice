package com.gleb.webservices.rest;

import java.util.List;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gleb.dao.objects.DBUser;
import com.gleb.webservices.bo.BOProduct;
import com.gleb.webservices.bo.BOProductListResponse;
import com.gleb.webservices.helpers.SecurityHelper;
import com.gleb.webservices.helpers.UserRoles;
import com.gleb.webservices.service.RecommendationService;

@Component
@Path("/recommendations")
public class RecomendationsResource {

    @Context
    private SecurityContext securityContext;

    @Autowired
    private SecurityHelper securityHelper;

    @Autowired
    private RecommendationService recomendationService;

    /**
     * Returns the list of recommended products depending on gender for current user
     * 
     * @summary Get recommended products
     * @param sessionId
     *            id of session
     * @param gender
     *            male/female
     * @return List of recommended products
     */
    // TODO return only items with mannequins images
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(UserRoles.ROLE_USER)
    public Response recommendations(@Context SecurityContext securityContext) {
        DBUser dbUser = securityHelper.getCurrentUser(securityContext);
        BOProductListResponse response = recomendationService.getRecomendations(dbUser);
        return Response.ok().entity(response).build();// productService.getRecommendations(securityHelper.getCurrentUser(securityContext));
    }

    @GET
    @Path("/byPriceTopCategoryAndStyle")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(UserRoles.ROLE_USER)
    public Response priceRecommendationByStylePriceAndTopCategory(@Context SecurityContext securityContext, @QueryParam("topCategory") String topCategory,
            @QueryParam("style") String style, @QueryParam("price") String price) {
        DBUser dbUser = securityHelper.getCurrentUser(securityContext);
        Set<BOProduct> response = recomendationService.getPriceRecommendationsByTopCategoryAndStyle(topCategory, style, dbUser);
        return Response.ok().entity(response).build();
    }

    @GET
    @Path("/byPriceAndTopCategory")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(UserRoles.ROLE_USER)
    public Response priceRecommendationsForPrice(@Context SecurityContext securityContext) {
        DBUser dbUser = securityHelper.getCurrentUser(securityContext);
        Set<BOProduct> response = recomendationService.getPriceRecommendations(dbUser);
        return Response.ok().entity(response).build();
    }

    @GET
    @Path("/byTopCategoryAndColours")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(UserRoles.ROLE_USER)
    public Response recommendationsByColour(@Context SecurityContext securityContext, @QueryParam("topCategory") String topCategory,
            @QueryParam("colour") List<String> colours) {
        DBUser dbUser = securityHelper.getCurrentUser(securityContext);
        Set<BOProduct> response = recomendationService.getRecommendationsByTopCategoryAndColoursIds(colours, topCategory, dbUser);
        return Response.ok().entity(response).build();
    }
}