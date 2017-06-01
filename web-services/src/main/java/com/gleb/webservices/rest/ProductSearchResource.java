package com.gleb.webservices.rest;

import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.gleb.webservices.bo.BOProductListResponse;
import com.gleb.webservices.helpers.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gleb.dao.objects.DBUser;
import com.gleb.webservices.bo.BOProduct;
import com.gleb.webservices.helpers.SecurityHelper;
import com.gleb.webservices.service.ProductService;

@Path("/products/search")
@Component
public class ProductSearchResource {

  @Autowired
  private ProductService productService;

  @Autowired
  private SecurityHelper securityHelper;

  @Context
  private SecurityContext securityContext;

  /**
   * Searches for all products that match input filters for current user
   * 
   * @summary Get all products using filters and/or text search
   * @param sessionId
   *            id of session
   * @return List of products
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed(UserRoles.ROLE_USER)
  public Response searchForProduct(@QueryParam("text") String searchText, @Context SecurityContext context) {
	BOProductListResponse products = productService.searchProducts(searchText, securityHelper.getCurrentUser(securityContext), 0);
	return Response.ok().entity(products).build();
  }

  @GET
  @Path("/filters")
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed(UserRoles.ROLE_USER)
  public Response searchForProductFilters(@BeanParam Filters filters, @QueryParam("from") Integer from) {
	DBUser currentUser = securityHelper.getCurrentUser(securityContext);
	BOProductListResponse products = productService.searchProductsByFilter(filters, currentUser, from == null ? 0 : from);
	return Response.ok().entity(products).build(); 
  }

  /**
   * Searches for all products that match input filters for current user
   * 
   * @summary Get all products using filters and/or text search
   * @param sessionId
   *            id of session
   * @return List of products
   */
  @GET
  @Path("/byCategory")
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed(UserRoles.ROLE_USER)
  public Response searchForProductByRootCategory(@QueryParam("text") String searchText, @QueryParam("topCategory") String rootCategory,
	  @QueryParam("from") Integer from, @Context SecurityContext context) {
	DBUser currentUser = securityHelper.getCurrentUser(securityContext);
	Set<BOProduct> products = productService.getProductsByTopCategory(rootCategory, searchText, currentUser, from == null ? 0 : from);
	return Response.ok().entity(products).build();
  }

}
