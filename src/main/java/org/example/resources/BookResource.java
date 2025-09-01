package org.example.resources;

import org.example.bean.Book;
import org.example.service.BookService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    private final BookService bookService;

    public BookResource(BookService bookService) {
        this.bookService = bookService;
    }

    @POST
    public Response createBook(Book book) {
        Book newBook = bookService.createBook(book);
        return Response.created(URI.create("/books/" + newBook.getId())).entity(newBook).build();
    }

    @GET
    public Response getBooks(@QueryParam("limit") @DefaultValue("10") int limit,
                             @QueryParam("offset") @DefaultValue("0") int offset) {
        try {
            List<Book> books = bookService.getAllBooks(limit, offset);
            return Response.ok(books).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while retrieving books.")
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getBook(@PathParam("id") long id) {
        Optional<Book> book = bookService.getBookById(id);
        return book.map(value -> Response.ok(value).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/search")
    public List<Book> searchBooksByTitle(@QueryParam("title") String title) {
        if (title == null || title.trim().isEmpty()) {
            return bookService.getAllBooks(20, 0);
        }
        return bookService.searchBooksByTitle(title);
    }

    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") long id, Book book) {
        Optional<Book> existingBook = bookService.getBookById(id);
        if (existingBook.isPresent()) {
            book.setId(id); // Ensure the ID from the path is used for the update
            bookService.updateBook(book);
            return Response.ok(book).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") long id) {
        Optional<Book> existingBook = bookService.getBookById(id);
        if (existingBook.isPresent()) {
            bookService.deleteBookById(id);
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}