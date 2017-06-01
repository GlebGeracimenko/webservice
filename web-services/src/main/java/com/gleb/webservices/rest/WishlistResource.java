package com.gleb.webservices.rest;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.gleb.webservices.helpers.UserRoles;
import com.gleb.webservices.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gleb.dao.objects.DBUser;
import com.gleb.webservices.bo.BOFullWishlist;
import com.gleb.webservices.service.WishlistService;

@Path("/wishlist")
@Component
public class WishlistResource {

    @Autowired
    private AuthService authService;

    @Autowired
    private WishlistService wishlistService;

    @GET
    @RolesAllowed(UserRoles.ROLE_USER)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWishlist(@Context SecurityContext context) {
        DBUser currentUser = authService.getCurrentUser(context);
        BOFullWishlist fullWishlist = wishlistService.getFullWishlist(currentUser.getId());
        return Response.ok().entity(fullWishlist).build();
    }
}
