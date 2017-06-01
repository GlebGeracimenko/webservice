package com.gleb.webservices.rest;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.gleb.webservices.helpers.UserRoles;
import com.gleb.webservices.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gleb.webservices.bo.BOImage;
import com.gleb.webservices.helpers.SecurityHelper;
import com.gleb.webservices.service.IdsService;

/**
 * Created by gleb on 27.10.15.
 */
@Component
@Path("/image")
public class ImageResource {

    @Autowired
    private ImageService imageService;

    @Context
    private SecurityContext securityContext;

    @Autowired
    private SecurityHelper securityHelper;
    
    @Autowired
    private IdsService idsService;

    @POST
    @Path("/save")
    @RolesAllowed(UserRoles.ROLE_IMPORTER)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveImage(BOImage boImage) {
        UUID id = imageService.saveImage(boImage);
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok().entity(id).build();
    }

    @PUT
    @Path("/update")
    @RolesAllowed(UserRoles.ROLE_IMPORTER)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateImage(BOImage boImage) {
        boolean status = imageService.updateImage(boImage);
        if (!status) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok().build();
    }

    @DELETE
    @Path("/delete")
    @RolesAllowed(UserRoles.ROLE_IMPORTER)
    public Response deleteImage(@QueryParam("id") String id) {
        boolean status = imageService.deleteImage(idsService.convert(id));
        if (!status) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok().build();
    }

    @GET
    @Path("/getByName/{name}")
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getImageByName(@PathParam("name") String name) {
        BOImage boImage = imageService.getImageByName(name);
        if (boImage == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(boImage).build();
    }

    @POST
    @Path("/getByIds")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getImageByIds(List<String> ids) {
        Collection<BOImage> boImages = imageService.getImageByIds(idsService.convert(ids));
        if (boImages.size() < 1) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(boImages).build();
    }

    @GET
    @Path("/getById/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getImageById(@PathParam("id") String id) {
        BOImage boImage = imageService.getImageById(idsService.convert(id));
        if (boImage == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(boImage).build();
    }

    @GET
    @Path("/getByInterLink/{internal_link}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getImageByInternalLink(@PathParam("internal_link") String internal_link) {
        BOImage boImage = imageService.getImageByInternalLink(internal_link);
        if (boImage == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(boImage).build();
    }

    @GET
    @Path("/getByExterLink/{external_link}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getImageByExternalLink(@PathParam("external_link") String external_link) {
        BOImage boImage = imageService.getImageByExternalLink(external_link);
        if (boImage == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(boImage).build();
    }

}
