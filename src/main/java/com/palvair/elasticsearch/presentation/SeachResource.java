package com.palvair.elasticsearch.presentation;

import com.palvair.elasticsearch.application.UserService;
import com.palvair.elasticsearch.presentation.error.EntityNotFoundException;
import com.palvair.elasticsearch.presentation.error.SearchBadRequest;
import com.palvair.elasticsearch.presentation.error.SearchError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Service
@Path("/searchExactly")
@Api(value = "/searchExactly", description = "Service de recherche")
public class SeachResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeachResource.class);
    @Autowired
    private UserService userService;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200,
                            response = SearchResult.class,
                            message = "Retourne le résultat de la recherche"),
                    @ApiResponse(code = 404,
                            response = EntityNotFoundException.class,
                            message = "Entité non trouvée"),
                    @ApiResponse(code = 400,
                            response = SearchBadRequest.class,
                            message = "Demande invalide"),
                    @ApiResponse(code = 500,
                            response = SearchError.class,
                            message = "Erreur pendant la recherche")
            }
    )
    public Response find(@QueryParam("value") final String value) {
        if (StringUtils.isBlank(value)) {
            final String message = "Value should not be blank";
            LOGGER.error(message);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new SearchBadRequest())
                    .build();
        }
        return Response.ok(
                userService.searchExactly(value)
        ).build();
    }
}
