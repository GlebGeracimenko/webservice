package com.gleb.webservices.rest;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.gleb.webservices.helpers.UserRoles;
import com.gleb.webservices.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gleb.webservices.bo.BOStore;

@Component
@Path("/stores")
public class StoresResource {

    @Autowired
    private StoreService storeService;

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(UserRoles.ROLE_USER)
    public Response getStoreById(@PathParam("id") String id) {
        BOStore boStore = storeService.getStoreById(id);
        if (boStore == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok().entity(boStore).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(UserRoles.ROLE_IMPORTER)
    public Response saveStore(BOStore boStore) {
        storeService.saveStore(boStore);
        return Response.ok().build();
    }

    @GET
    @Path("/bynetwork/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(UserRoles.ROLE_USER)
    public Response getStoresByNetworkId(@PathParam("id") Long networkId) {
        return null;
    }
}
