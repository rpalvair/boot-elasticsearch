package com.palvair.elasticsearch.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Service
@Path("/search")
@Api(value = "/search", description = "Service de recherche")
public class SeachResource {

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiResponses(
            @ApiResponse(code = 200,
                    response = String.class,
                    message = "Retourne le résultat de la recherche")
    )
    public Response find() {
        return Response.ok()
                .build();
    }
}
