package com.gleb.webservices.rest;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import com.gleb.webservices.bo.UserIdToken;
import com.gleb.webservices.helpers.UserRoles;
import com.gleb.webservices.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.gleb.dao.objects.DBToken;
import com.gleb.dao.objects.DBUser;
import com.gleb.dao.objects.TokenState;
import com.gleb.webservices.helpers.SecurityHelper;
import com.gleb.webservices.service.IdsService;
import com.gleb.webservices.service.UserTokenService;

@Path("/token")
@Component
public class TokenResource {

    @Autowired
    private SecurityHelper securityHelper;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    private IdsService idsService;

    @GET
    @Path("/renew")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRenewedToken(@Context SecurityContext securityContext, @QueryParam("userId") String userId, @QueryParam("token") String userToken)
            throws URISyntaxException, UnsupportedEncodingException {
        DBToken oldToken = userTokenService.getTokenInfo(idsService.convert(userId), userToken);
        if (oldToken == null) {
            return Response.status(Status.BAD_REQUEST).entity("haven't valid token").build();
        }
        if (!oldToken.getToken().equals(userToken)) {
            return Response.status(Status.BAD_REQUEST).entity("valid token is not the same as specified").build();
        }
        if (!oldToken.getState().equals(TokenState.EXPIRED.name())) {
            return Response.status(Status.BAD_REQUEST).entity("wrong token state").build();
        }
        String newToken = userTokenService.renewToken(idsService.convert(userId), oldToken.getToken());
        Map<String, String> token = new HashMap<String, String>();
        token.put("token", newToken);
        return Response.ok().entity(token).build();
    }

    @GET
    @Path("/new")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(UserRoles.ROLE_USER)
    public Response getNewToken(@Context SecurityContext securityContext) throws URISyntaxException, UnsupportedEncodingException {
        DBUser dbUser = securityHelper.getCurrentUser(securityContext);
        if (dbUser == null) {
            Map<String, String> token = new HashMap<String, String>();
            token.put("token", "");
            return Response.ok().entity(token).build();
        }
        String currentToken = userTokenService.getValidTokenForUser(dbUser.getId());
        if (currentToken != null && userTokenService.isTokenExpired(dbUser.getId(), currentToken)) {
            currentToken = userTokenService.renewToken(dbUser.getId(), currentToken);
        }
        if (currentToken == null) {
            currentToken = userTokenService.createTokenForUser(dbUser.getId());
        }
        Map<String, String> token = new HashMap<String, String>();
        token.put("token", currentToken);
        return Response.ok().entity(token).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Context HttpServletRequest request, UserIdToken userIdToken) throws URISyntaxException, UnsupportedEncodingException {
        DBUser dbUser = userTokenService.getUserByToken(idsService.convert(userIdToken.getUserId()), userIdToken.getToken());
        if (dbUser == null) {
            return Response.status(Status.FORBIDDEN).build();
        }
        Map<String, Object> response = new HashMap<String, Object>();
        if (userTokenService.isTokenExpired(idsService.convert(userIdToken.getUserId()), userIdToken.getToken())) {
            response.put("loggedIn", false);
            response.put("expired", true);
            return Response.status(Status.FORBIDDEN).entity(response).build();
        }
        response.put("loggedIn", true);
        response.put("expired", false);
        authService.authorizeUser(dbUser);
        request.getSession().setMaxInactiveInterval(6000);
        return Response.ok().entity(response).build();
    }

    @GET
    @Path("/logout")
    public Response logout(@Context HttpServletRequest request) throws UnsupportedEncodingException, URISyntaxException {
        SecurityContextHolder.getContext().setAuthentication(null);
        request.getSession().invalidate();
        return Response.ok().build();
    }
}
