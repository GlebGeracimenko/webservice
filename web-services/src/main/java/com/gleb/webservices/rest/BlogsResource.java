package com.gleb.webservices.rest;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.gleb.webservices.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gleb.webservices.bo.BOBlogPost;
import com.gleb.webservices.helpers.UserRoles;

@Component
@Path("/blog")
public class BlogsResource {

    @Autowired
    private BlogService blogService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(UserRoles.ROLE_USER)
    public Response getBlogPosts() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -3);
        Collection<BOBlogPost> blogPosts = blogService.getPosts(calendar.getTime(), 50);
        return Response.ok().entity(blogPosts).build();
    }
}