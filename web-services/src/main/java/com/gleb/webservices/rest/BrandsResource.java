package com.gleb.webservices.rest;

import java.util.Collection;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
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

import com.gleb.webservices.helpers.UserRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gleb.dao.objects.DBUser;
import com.gleb.webservices.bo.BOBrand;
import com.gleb.webservices.bo.BOBrandWithFollowing;
import com.gleb.webservices.helpers.SecurityHelper;
import com.gleb.webservices.service.BrandsService;

@Component
@Path("/brands")
public class BrandsResource {

    private static Logger logger = LoggerFactory.getLogger(BrandsResource.class);

    @Autowired
    private BrandsService brandsService;

    @Context
    private SecurityContext securityContext;

    @Autowired
    private SecurityHelper securityHelper;

    /**
     * Save Brand to database
     * 
     * @param brand
     * @return new Id for current Brand
     */
    @POST
    @RolesAllowed(UserRoles.ROLE_IMPORTER)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveProduct(BOBrand boBrand) {
        String id = brandsService.saveBrand(boBrand);
        if (id == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        return Response.ok().entity(id).build();
    }

    @Path("/{id}")
    @GET
    @RolesAllowed(UserRoles.ROLE_USER)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProduct(@PathParam("id") String brandId) {
        DBUser currentUser = securityHelper.getCurrentUser(securityContext);
        BOBrandWithFollowing brand = brandsService.getBrand(brandId, currentUser.getId());
        if (brand == null) {
            return Response.status(Status.NOT_FOUND).build();
        } else {
            return Response.ok().entity(brand).build();
        }
    }

    @GET
    @RolesAllowed(UserRoles.ROLE_USER)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProducts(@QueryParam("from") int from) {
        DBUser currentUser = securityHelper.getCurrentUser(securityContext);
        Collection<BOBrandWithFollowing> brands = brandsService.getAllBrands(currentUser, from);
        return Response.ok().entity(brands).build();
    }

    @POST
    @Path("/follow/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(UserRoles.ROLE_USER)
    public Response follow(@PathParam("id") String brandId) {
        DBUser currentUser = securityHelper.getCurrentUser(securityContext);
        if (brandId == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        BOBrandWithFollowing boBrand = brandsService.getBrand(brandId, currentUser.getId());
        if (boBrand == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        brandsService.followBrand(currentUser.getId(), brandId);
        logger.info("User id:" + currentUser.getId() + " follows brand id:" + brandId);
        return Response.ok().build();

    }

    @POST
    @Path("/unfollow/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(UserRoles.ROLE_USER)
    public Response unfollow(@PathParam("id") String brandId) {
        DBUser currentUser = securityHelper.getCurrentUser(securityContext);
        if (brandId == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        BOBrandWithFollowing boBrand = brandsService.getBrand(brandId, currentUser.getId());
        if (boBrand == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        brandsService.unfollowBrand(currentUser.getId(), brandId);
        return Response.ok().build();
    }

    @GET
    @Path("/following")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(UserRoles.ROLE_USER)
    public Response followingBrands() {
        DBUser currentUser = securityHelper.getCurrentUser(securityContext);
        Collection<BOBrand> brands = brandsService.getAllBrandsFollowedByUser(currentUser.getId());
        return Response.ok().entity(brands).build();
    }

}
