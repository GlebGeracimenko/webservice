package com.gleb.webservices.rest;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.gleb.webservices.helpers.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gleb.webservices.bo.BOAvatar2DParams;
import com.gleb.webservices.helpers.JsonHelper;
import com.gleb.webservices.service.Avatar2DService;

@Component
@Path("/2davatar")
public class Avatar2DResource {

    @Autowired
    private Avatar2DService avatar2DService;

    @Autowired
    private JsonHelper jsonHelper;

    @GET
    @RolesAllowed(UserRoles.ROLE_USER)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAvailableParams() {
        BOAvatar2DParams params = new BOAvatar2DParams();
        params.setBodySizes(avatar2DService.getAvailableAvatarSizes());
        params.setHairColours(avatar2DService.getAvailableHairColours());
        params.setHairSizes(avatar2DService.getAvailableHairSizes());
        params.setSkinColours(avatar2DService.getAvailableSkinColours());
        return Response.ok().entity(params).build();
    }

    @GET
    @Path("/link")
    @RolesAllowed(UserRoles.ROLE_USER)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLink(@QueryParam("hairSize") String hairSize, @QueryParam("hairColour") String hairColour, @QueryParam("bodySize") String bodySize,
            @QueryParam("skinColour") String skinColour, @QueryParam("topCode") String topCode, @QueryParam("bottomCode") String bottomCode,
            @QueryParam("shoesCode") String shoesCode, @QueryParam("bagCode") String bagCode) {
        String link = avatar2DService.constructLink(skinColour, hairColour, bodySize, hairSize, topCode, bottomCode, shoesCode, bagCode);
        return Response.ok().entity(jsonHelper.makeAsMap("link", link)).build();
    }
}
