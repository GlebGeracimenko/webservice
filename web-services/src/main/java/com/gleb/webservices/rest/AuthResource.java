package com.gleb.webservices.rest;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.gleb.dao.UsersDAO;
import com.gleb.dao.objects.DBUser;
import com.gleb.webservices.bo.LoginPassword;
import com.gleb.webservices.helpers.JsonHelper;
import com.gleb.webservices.helpers.UserRoles;
import com.gleb.webservices.service.AuthService;
import com.gleb.webservices.service.UserService;

@Path("/auth")
@Component
public class AuthResource {

    @Value("${facebook.clientId}")
    private String facebookClientId;

    @Value("${facebook.clientSecret}")
    private String facebookClientSecret;

    @Value("${facebook.redirectPrefix}")
    private String facebookRedirectPrefix;

    @Autowired
    private UsersDAO userDAO;

    @Autowired
    private JsonHelper jsonHelper;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;
//{"login":"test", "passwordBase64":"test", "email":"someemail@example.com"}
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Context HttpServletRequest request, LoginPassword loginPassword) throws URISyntaxException, UnsupportedEncodingException {
        if (loginPassword == null) {
            return Response.status(Status.FORBIDDEN).build();
        }
        if (loginPassword.getPasswordBase64() == null) {
            return Response.status(Status.FORBIDDEN).build();
        }
        if (loginPassword.getLogin() == null) {
            return Response.status(Status.FORBIDDEN).build();
        }
        DBUser user = userDAO.getUserByLogin(loginPassword.getLogin());
        if (user == null) {
            return Response.status(Status.FORBIDDEN).build();
        }
        if (!user.getPassword().equals(loginPassword.getPasswordBase64())) {
            return Response.status(Status.FORBIDDEN).build();
        }
        authService.authorizeUser(user);
        request.getSession().setMaxInactiveInterval(6000);
        return Response.ok().build();
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerStub(LoginPassword loginPassword) throws URISyntaxException, UnsupportedEncodingException {
        DBUser user = userService.findUserByLoginAndEmail(loginPassword.getLogin(), loginPassword.getEmail());
        if (user != null) {
            return Response.status(Status.FORBIDDEN).build();
        }
        user = new DBUser();
        user.setLogin(loginPassword.getLogin());
        user.setEmail(loginPassword.getEmail());
        user.setPassword(loginPassword.getPasswordBase64());
        user.setCreatedAt(new Date());
        user.setGroups(Arrays.asList(UserRoles.ROLE_USER));
        UUID id = userService.saveUser(user);
        authService.authorizeUser(user);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("id", id);
        return Response.ok().entity(result).build();
    }

    @GET
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(@Context HttpServletRequest request) throws UnsupportedEncodingException, URISyntaxException {
        SecurityContextHolder.getContext().setAuthentication(null);
        request.getSession().invalidate();
        return Response.ok().build();
    }

    @GET
    @Path("/oauth/loginString/fb")
    public String constructFBLoginString() {
        StringBuilder builder = new StringBuilder("https://www.facebook.com/dialog/oauth?");
        builder.append("client_id=" + facebookClientId);
        builder.append("&redirect_uri=" + facebookRedirectPrefix);
        builder.append("&response_type=code");
        builder.append("&display=popup");
        String abc = builder.toString();
        return abc;
    }

    @GET
    @Path("/fbcallback")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fbAuthCallback(@Context HttpServletRequest request, @QueryParam("access_token") String token) throws URISyntaxException {
        DBUser dbUser = userService.getDBUserByFaceBookId(token);
        authService.authorizeUser(dbUser);
        return Response.ok().entity(jsonHelper.makeAsMap("id", dbUser.getId())).build();
    }
}
