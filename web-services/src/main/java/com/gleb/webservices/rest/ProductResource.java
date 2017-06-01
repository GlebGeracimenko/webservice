package com.gleb.webservices.rest;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import com.gleb.webservices.bo.BOProductListResponse;
import com.gleb.webservices.helpers.UserRoles;
import com.gleb.webservices.service.LikesService;
import com.gleb.webservices.service.WishlistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gleb.dao.objects.DBUser;
import com.gleb.webservices.bo.BOProduct;
import com.gleb.webservices.helpers.SecurityHelper;
import com.gleb.webservices.mapping.BOProductsList;
import com.gleb.webservices.service.ProductService;

@Component
@Path("/products")
public class ProductResource {
    private static Logger logger = LoggerFactory.getLogger(ProductResource.class);
    @Autowired
    private ProductService productService;

    @Context
    private SecurityContext securityContext;

    @Autowired
    private SecurityHelper securityHelper;

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private LikesService likesService;

    /**
     * Save Product to database
     * 
     * @param product
     * @return new Id for current Product
     */
    @POST
    @RolesAllowed(UserRoles.ROLE_IMPORTER)
    @Consumes(MediaType.APPLICATION_JSON)
    public String saveProduct(BOProduct product) {
        return productService.saveProduct(product);
    }

    /**
     * delete Product from database
     * 
     * @param product
     * @return new Id for current Product
     */
    @DELETE
    @Path("/{id}")
    @RolesAllowed(UserRoles.ROLE_IMPORTER)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteProduct(@PathParam("id") String productId) {
        productService.deleteProduct(productId);
        return Response.ok().build();
    }

    /**
     * Save Product to database
     * 
     * @param product
     * @return new Id for current Product
     */
    @POST
    @Path("/batch")
    @RolesAllowed(UserRoles.ROLE_IMPORTER)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveProduct(BOProductsList list) {
        for (BOProduct boProduct : list.getProducts()) {
            productService.saveProduct(boProduct);
        }
        return Response.ok().build();
    }

    @Path("/{id}")
    @GET
    @RolesAllowed(UserRoles.ROLE_USER)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProduct(@PathParam("id") String productId) {
        BOProduct product = productService.getProduct(productId);
        if (product == null) {
            return Response.status(Status.NOT_FOUND).build();
        } else {
            return Response.ok().entity(product).build();
        }
    }

    @GET
    @RolesAllowed(UserRoles.ROLE_USER)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProducts(@QueryParam("from") int from) {
        DBUser currentUser = securityHelper.getCurrentUser(securityContext);
        BOProductListResponse products = productService.searchProducts("", currentUser, from);
        return Response.ok().entity(products).build();
    }

    /**
     * Marks the product as 'liked' for current user
     * 
     * @summary Like the product
     * @param sessionId
     *            id of session
     * @param itemSku
     *            product SKU
     * @return Status true/false
     */
    @GET
    @Path("/like/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(UserRoles.ROLE_USER)
    public Response like(@PathParam("id") String productId) {
        DBUser currentUser = securityHelper.getCurrentUser(securityContext);
        BOProduct boProduct = productService.getProduct(productId);
        if (boProduct == null) {
            logger.info("Result is empty for id: {" + productId + "}");
            return Response.status(Status.NOT_FOUND).build();
        }
        wishlistService.addLikedItem(currentUser.getId(), productId);
        likesService.likeProduct(productId, currentUser.getId());
        return Response.ok().build();
    }

    /**
     * Marks the product as 'disliked' for current user
     * 
     * @summary Dislike the product
     * @param sessionId
     *            id of session
     * @param itemSku
     *            product SKU
     * @return Status true/false
     */
    @GET
    @Path("/dislike/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(UserRoles.ROLE_USER)
    public Response dislike(@PathParam("id") String productId) {
        DBUser currentUser = securityHelper.getCurrentUser(securityContext);
        BOProduct boProduct = productService.getProduct(productId);
        if (boProduct == null) {
            logger.info("Result is empty for id: {" + productId + "}");
            return Response.status(Status.NOT_FOUND).build();
        }
        wishlistService.addDislikedItem(currentUser.getId(), productId);
        likesService.dislikeProduct(productId, currentUser.getId());
        return Response.ok().build();
    }
}
