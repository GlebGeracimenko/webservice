package com.gleb.webservices.rest;

import java.util.Collection;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.gleb.webservices.helpers.UserRoles;
import com.gleb.webservices.service.StyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gleb.dao.objects.DBUser;
import com.gleb.webservices.bo.BOStyle;
import com.gleb.webservices.helpers.JsonHelper;
import com.gleb.webservices.helpers.SecurityHelper;

@Component
@Path("/styles")
public class StylesResource {

	@Autowired
	private StyleService styleService;

	@Context
	private SecurityContext securityContext;

	@Autowired
	private SecurityHelper securityHelper;

	@Autowired
	private JsonHelper jsonHelper;

	/**
	 * Save Brand to database
	 * 
	 * @param brand
	 * @return new Id for current Brand
	 */
	@POST
	@RolesAllowed(UserRoles.ROLE_IMPORTER)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveStyle(BOStyle boStyle) {
		String id = styleService.saveStyle(boStyle);
		return Response.ok().entity(jsonHelper.makeAsMap("id", id)).build();
	}

	@Path("/{id}")
	@GET
	@RolesAllowed(UserRoles.ROLE_USER)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStyle(@PathParam("id") String styleId) {
		BOStyle style = styleService.getStyle(styleId);
		return Response.ok().entity(style).build();
	}

	@GET
	@RolesAllowed(UserRoles.ROLE_USER)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllStyles() {
		Collection<BOStyle> allStyles = styleService.getAll();
		return Response.ok().entity(allStyles).build();
	}

	@POST
	@Path("/follow/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed(UserRoles.ROLE_USER)
	public Response follow(@PathParam("id") String styleId) {
		DBUser currentUser = securityHelper.getCurrentUser(securityContext);
		styleService.followStyle(currentUser, styleId);
		return Response.ok().build();

	}

	@POST
	@Path("/unfollow/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed(UserRoles.ROLE_USER)
	public Response unfollow(@PathParam("id") String styleId) {
		DBUser currentUser = securityHelper.getCurrentUser(securityContext);
		styleService.unfollowStyle(currentUser, styleId);
		return Response.ok().build();
	}

	@GET
	@Path("/following")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed(UserRoles.ROLE_USER)
	public Response followingStyles() {
		DBUser currentUser = securityHelper.getCurrentUser(securityContext);
		Collection<BOStyle> styles = styleService.followingStyles(currentUser);
		return Response.ok().entity(styles).build();
	}

}
