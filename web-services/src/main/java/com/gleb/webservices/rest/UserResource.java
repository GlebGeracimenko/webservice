package com.gleb.webservices.rest;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
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
import org.springframework.beans.factory.annotation.Autowired;

import com.gleb.dao.objects.DBUser;
import com.gleb.webservices.bo.BOUser;
import com.gleb.webservices.helpers.SecurityHelper;
import com.gleb.webservices.mapping.UserMapper;
import com.gleb.webservices.service.UserService;

@Path("/me")
public class UserResource {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityHelper securityHelper;

    @GET
    @RolesAllowed(UserRoles.ROLE_USER)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyInfo(@Context SecurityContext securityContext) {
        DBUser dbUser = securityHelper.getCurrentUser(securityContext);
        BOUser boUser = userMapper.map(dbUser);
        return Response.ok().entity(boUser).build();
    }

    @POST
    @RolesAllowed(UserRoles.ROLE_USER)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateMyInfo(@Context SecurityContext securityContext, BOUser boUser) {
        DBUser dbUser = securityHelper.getCurrentUser(securityContext);
        dbUser = userMapper.map(dbUser, boUser);
        userService.saveUser(dbUser);
        return Response.ok().build();
    }


    @POST
    @Path("/wizardState/{page}/{value}")
    @RolesAllowed(UserRoles.ROLE_USER)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addWizardStep(@PathParam("page") String page, @PathParam("value") Boolean value, @Context SecurityContext securityContext) {
        DBUser currentUser = securityHelper.getCurrentUser(securityContext);
        Map<String, Boolean> wizardState = currentUser.getWizardState();
        if (wizardState == null) {
            wizardState = new HashMap<String, Boolean>();
            currentUser.setWizardState(wizardState);
        }
        wizardState.put(page, value);
        userService.saveUser(currentUser);
        return Response.ok().build();
    }

    @GET
    @Path("/wizardState")
    @RolesAllowed(UserRoles.ROLE_USER)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWizardStep(@Context SecurityContext securityContext) {
        DBUser currentUser = securityHelper.getCurrentUser(securityContext);
        Map<String, Boolean> wizardState = currentUser.getWizardState();
        if (wizardState == null) {
        	wizardState = new HashMap<String, Boolean>();
        	currentUser.setWizardState(wizardState);
        	userService.saveUser(currentUser);
        }
        return Response.ok().entity(wizardState).build();
    }
}
