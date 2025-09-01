package org.example.resources;

import org.example.bean.Author;
import org.example.service.AuthorService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
public class AuthorResource {

    private final AuthorService authorService;

    public AuthorResource(AuthorService authorService) {
        this.authorService = authorService;
    }

    @POST
    public Response createAuthor(Author author) {
        Author createdAuthor = authorService.createAuthor(author);
        return Response.status(Response.Status.CREATED).entity(createdAuthor).build();
    }

    @GET
    public Response getAllAuthors(@QueryParam("limit") @DefaultValue("10") int limit,
                                  @QueryParam("offset") @DefaultValue("0") int offset) {
        try {
            List<Author> authors = authorService.getAllAuthors(limit, offset);
            return Response.ok(authors).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while retrieving authors.")
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getAuthorById(@PathParam("id") int id) {
        Optional<Author> author = authorService.getAuthorById(id);
        return author.map(value -> Response.ok(value).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/search")
    public List<Author> searchAuthorsByName(@QueryParam("authorName") String name) {
        // If no name is provided, return an empty list or handle as per business logic.
        // The service layer handles the wildcard search.
        if (name == null || name.trim().isEmpty()) {
            return authorService.getAllAuthors(20, 0); // Return first 20 authors
        }
        return authorService.searchAuthorsByName(name);
    }

    @PUT
    @Path("/{id}")
    public Response updateAuthor(@PathParam("id") int id, Author author) {
        Optional<Author> existingAuthor = authorService.getAuthorById(id);
        if (existingAuthor.isPresent()) {
            author.setId(id);
            authorService.updateAuthor(author);
            return Response.ok(author).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") int id) {
        Optional<Author> existingAuthor = authorService.getAuthorById(id);
        if (existingAuthor.isPresent()) {
            authorService.deleteAuthorById(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}