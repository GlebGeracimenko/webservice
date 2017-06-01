package com.gleb.webservices.rest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.gleb.webservices.helpers.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gleb.webservices.service.ProductService;

@Component
@Path("/categories")
public class CategoriesResource {

    private static List<String> topCategories = new ArrayList<String>();
    private static List<String> styles = new ArrayList<String>();

    @Autowired
    private ProductService productService;

    static {
        topCategories.add("top");
        topCategories.add("bottom");
        topCategories.add("outerwear");
//        topCategories.add("underwear");
        topCategories.add("shoes");
        topCategories.add("accessories");
        topCategories.add("bag");
        topCategories.add("hat");

        styles.add("casual");
        styles.add("business");
        styles.add("sport");
        styles.add("natural");
        styles.add("gothic");
    }

    @GET
    @Path("/allTop")
    @RolesAllowed(UserRoles.ROLE_USER)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAllTopCategories() {
        return Response.ok().entity(topCategories).build();
    }
}
