package com.gleb.webservices.rest;

import java.util.Collection;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gleb.webservices.bo.BOColour;
import com.gleb.webservices.bo.BOColourCombination;
import com.gleb.webservices.helpers.UserRoles;
import com.gleb.webservices.service.ColourService;

/**
 * Created by gleb on 16.09.15.
 */

@Component
@Path("/colour")
public class ColourResource {

  @Autowired
  private ColourService colourService;

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed(UserRoles.ROLE_IMPORTER)
  public Response saveColor(BOColour boColor) {
    colourService.saveColour(boColor);
	return Response.ok().build();
  }

  @GET
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed(UserRoles.ROLE_USER)
  public Response getColorById(@PathParam("id") String id) {
	BOColour boColor = colourService.getColourById(id);
	if (boColor == null) {
	  return Response.status(Response.Status.NOT_FOUND).build();
	} else {
	  return Response.ok().entity(boColor).build();
	}
  }

  @GET
  @Path("/combinations")
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed(UserRoles.ROLE_USER)
  public Response getColourCombinations() {
	Collection<BOColourCombination> boColourCombinations = colourService.getAllColourCombinations();
	return Response.ok().entity(boColourCombinations).build();
  }

  @GET
  @Path("/combinations/{colourName}")
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed(UserRoles.ROLE_USER)
  public Response getColourCombinationsForColour(@PathParam("colourName") String colourName) {
	Collection<BOColourCombination> boColourCombinations = colourService.getColourCombinationsByColourName(colourName);
	return Response.ok().entity(boColourCombinations).build();
  }

  @GET
  @Path("/combinations/{colourName}/{combinationName}")
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed(UserRoles.ROLE_USER)
  public Response getColourCombinationsForColour(@PathParam("colourName") String colourName, @PathParam("combinationName") String combinationName) {
	BOColourCombination boColourCombination = colourService.getColourCombinationsByColourNameAndType(colourName, combinationName);
	return Response.ok().entity(boColourCombination).build();
  }
}