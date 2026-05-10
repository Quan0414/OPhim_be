package com.example.controller;

import com.example.service.OPhimService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/v1/api")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "OPhim", description = "Proxy endpoints for OPhim movie APIs")
public class OPhimController {
    private final OPhimService service;

    @Inject
    public OPhimController(OPhimService service) {
        this.service = service;
    }

    @GET
    @Path("/home")
    @Operation(summary = "Get home movie list", description = "Proxy OPhim home endpoint.")
    @APIResponse(responseCode = "200", description = "OPhim JSON response")
    public JsonNode home() {
        return service.home();
    }

    @GET
    @Path("/danh-sach/{slug}")
    @Operation(summary = "Get movies by list slug", description = "Supports paging, sorting, category, country, and year filters.")
    @APIResponse(responseCode = "200", description = "OPhim JSON response")
    @APIResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = com.example.dto.ErrorResponse.class)))
    public JsonNode listByFilter(
            @Parameter(description = "List slug, e.g. phim-moi, phim-bo, phim-le") @PathParam("slug") String slug,
            @Parameter(description = "Page number") @QueryParam("page") @DefaultValue("1") Integer page,
            @Parameter(description = "Items per page, max 100") @QueryParam("limit") @DefaultValue("24") Integer limit,
            @Parameter(description = "Sort field: modified.time, year, _id") @QueryParam("sort_field") String sortField,
            @Parameter(description = "Sort type: asc or desc") @QueryParam("sort_type") String sortType,
            @Parameter(description = "Category slugs separated by comma") @QueryParam("category") String category,
            @Parameter(description = "Country slugs separated by comma") @QueryParam("country") String country,
            @Parameter(description = "Release year") @QueryParam("year") String year
    ) {
        return service.listByFilter(slug, page, limit, sortField, sortType, category, country, year);
    }

    @GET
    @Path("/phim/{slug}")
    @Operation(summary = "Get movie detail", description = "Get detail, metadata, and episodes by movie slug.")
    @APIResponse(responseCode = "200", description = "OPhim JSON response")
    @APIResponse(responseCode = "400", description = "Invalid slug", content = @Content(schema = @Schema(implementation = com.example.dto.ErrorResponse.class)))
    public JsonNode movieDetail(@PathParam("slug") String slug) {
        return service.movieDetail(slug);
    }

    @GET
    @Path("/phim/{slug}/images")
    @Operation(summary = "Get movie images", description = "Get poster and backdrop images by movie slug.")
    @APIResponse(responseCode = "200", description = "OPhim JSON response")
    public JsonNode movieImages(@PathParam("slug") String slug) {
        return service.movieImages(slug);
    }

    @GET
    @Path("/phim/{slug}/keywords")
    @Operation(summary = "Get movie keywords", description = "Get TMDB keywords by movie slug.")
    @APIResponse(responseCode = "200", description = "OPhim JSON response")
    public JsonNode movieKeywords(@PathParam("slug") String slug) {
        return service.movieKeywords(slug);
    }

    @GET
    @Path("/phim/{slug}/peoples")
    @Operation(summary = "Get movie peoples", description = "Get actors and directors by movie slug.")
    @APIResponse(responseCode = "200", description = "OPhim JSON response")
    public JsonNode moviePeoples(@PathParam("slug") String slug) {
        return service.moviePeoples(slug);
    }

    @GET
    @Path("/tim-kiem")
    @Operation(summary = "Search movies", description = "Search movies by keyword.")
    @APIResponse(responseCode = "200", description = "OPhim JSON response")
    @APIResponse(responseCode = "400", description = "Invalid keyword or paging", content = @Content(schema = @Schema(implementation = com.example.dto.ErrorResponse.class)))
    public JsonNode search(
            @Parameter(description = "Search keyword, at least 2 characters", required = true, in = ParameterIn.QUERY) @QueryParam("keyword") String keyword,
            @Parameter(description = "Page number") @QueryParam("page") @DefaultValue("1") Integer page,
            @Parameter(description = "Items per page, max 100") @QueryParam("limit") @DefaultValue("24") Integer limit
    ) {
        return service.search(keyword, page, limit);
    }

    @GET
    @Path("/the-loai")
    @Operation(summary = "Get categories", description = "Get all OPhim movie categories.")
    @APIResponse(responseCode = "200", description = "OPhim JSON response")
    public JsonNode categories() {
        return service.categories();
    }

    @GET
    @Path("/the-loai/{slug}")
    @Operation(summary = "Get movies by category", description = "Get movies by category slug.")
    @APIResponse(responseCode = "200", description = "OPhim JSON response")
    public JsonNode moviesByCategory(
            @PathParam("slug") String slug,
            @QueryParam("page") @DefaultValue("1") Integer page,
            @QueryParam("limit") @DefaultValue("24") Integer limit,
            @QueryParam("sort_field") String sortField,
            @QueryParam("sort_type") String sortType,
            @QueryParam("country") String country,
            @QueryParam("year") String year
    ) {
        return service.moviesByCategory(slug, page, limit, sortField, sortType, country, year);
    }

    @GET
    @Path("/quoc-gia")
    @Operation(summary = "Get countries", description = "Get all OPhim countries.")
    @APIResponse(responseCode = "200", description = "OPhim JSON response")
    public JsonNode countries() {
        return service.countries();
    }

    @GET
    @Path("/quoc-gia/{slug}")
    @Operation(summary = "Get movies by country", description = "Get movies by country slug.")
    @APIResponse(responseCode = "200", description = "OPhim JSON response")
    public JsonNode moviesByCountry(
            @PathParam("slug") String slug,
            @QueryParam("page") @DefaultValue("1") Integer page,
            @QueryParam("limit") @DefaultValue("24") Integer limit,
            @QueryParam("sort_field") String sortField,
            @QueryParam("sort_type") String sortType,
            @QueryParam("category") String category,
            @QueryParam("year") String year
    ) {
        return service.moviesByCountry(slug, page, limit, sortField, sortType, category, year);
    }

    @GET
    @Path("/nam-phat-hanh")
    @Operation(summary = "Get release years", description = "Get all OPhim release years.")
    @APIResponse(responseCode = "200", description = "OPhim JSON response")
    public JsonNode years() {
        return service.years();
    }

    @GET
    @Path("/nam-phat-hanh/{year}")
    @Operation(summary = "Get movies by release year", description = "Get movies by release year.")
    @APIResponse(responseCode = "200", description = "OPhim JSON response")
    @APIResponse(responseCode = "400", description = "Invalid year or paging", content = @Content(schema = @Schema(implementation = com.example.dto.ErrorResponse.class)))
    public JsonNode moviesByYear(
            @PathParam("year") String year,
            @QueryParam("page") @DefaultValue("1") Integer page,
            @QueryParam("limit") @DefaultValue("24") Integer limit,
            @QueryParam("sort_field") String sortField,
            @QueryParam("sort_type") String sortType,
            @QueryParam("category") String category,
            @QueryParam("country") String country
    ) {
        return service.moviesByYear(year, page, limit, sortField, sortType, category, country);
    }
}
