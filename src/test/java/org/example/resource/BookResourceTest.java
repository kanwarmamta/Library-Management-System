package org.example.resource;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.example.bean.Book;
import org.example.resources.BookResource;
import org.example.service.BookService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(DropwizardExtensionsSupport.class)
class BookResourceTest {

    // Mock the BookService dependency
    private static final BookService bookService = Mockito.mock(BookService.class);

    // Create a ResourceExtension to test the BookResource
    public static final ResourceExtension resources = ResourceExtension.builder()
            .addResource(new BookResource(bookService))
            .build();

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = new Book(1, "The Hobbit", 1L, new Date(), "12345");
    }

    @AfterEach
    void tearDown() {
        // Reset the mock after each test to ensure a clean slate
        reset(bookService);
    }

    @Test
    void testCreateBook() {
        when(bookService.createBook(Mockito.any(Book.class))).thenReturn(testBook);

        Response response = resources.target("/books")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(testBook, MediaType.APPLICATION_JSON));

        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.CREATED.getStatusCode());
        assertThat(response.readEntity(Book.class)).isEqualTo(testBook);
    }

    @Test
    void testGetBookByIdSuccess() {
        when(bookService.getBookById(1L)).thenReturn(Optional.of(testBook));

        Book book = resources.target("/books/1").request().get(Book.class);

        assertThat(book.getId()).isEqualTo(testBook.getId());
    }

    @Test
    void testGetBookByIdNotFound() {
        when(bookService.getBookById(2L)).thenReturn(Optional.empty());

        Response response = resources.target("/books/2").request().get();

        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void testGetAllBooks() {
        // Correctly mock the method with pagination parameters
        when(bookService.getAllBooks(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(Collections.singletonList(testBook));

        List<Book> books = resources.target("/books").request().get(new GenericType<List<Book>>() {});

        assertThat(books).isNotEmpty();
    }
}
