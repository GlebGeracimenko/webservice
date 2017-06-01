package com.gleb.webservices.rest;

import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gleb.webservices.bo.BOVirtualStylistCanvas;
import com.gleb.webservices.helpers.UserRoles;
import com.gleb.webservices.service.IdsService;
import com.gleb.webservices.service.VirtualStylistService;

/**
 * Created by gleb on 16.09.15.
 */

@Component
@Path("/virtualStylist")
public class VirtualStylistResource {

    @Autowired
    private VirtualStylistService stylistService;

    @Autowired
    private IdsService idsService;

    @POST
    @Path("/send")
    @RolesAllowed(UserRoles.ROLE_IMPORTER)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response send(BOVirtualStylistCanvas canvas) {
        UUID id = stylistService.save(canvas);
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok().entity(id).build();
    }

    @DELETE
    @Path("/delete/{id}")
    @RolesAllowed(UserRoles.ROLE_IMPORTER)
    public Response delete(@PathParam("id") String id) {
        boolean status = stylistService.delete(idsService.convert(id));
        if (!status) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok().build();
    }

    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") String id) {
        BOVirtualStylistCanvas canvas = stylistService.getById(idsService.convert(id));
        if (canvas == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(canvas).build();
    }

}
