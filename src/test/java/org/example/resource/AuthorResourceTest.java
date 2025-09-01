package org.example.resource;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.example.bean.Author;
import org.example.resources.AuthorResource;
import org.example.service.AuthorService;
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
class AuthorResourceTest {

    private static final AuthorService authorService = Mockito.mock(AuthorService.class);

    public static final ResourceExtension resources = ResourceExtension.builder()
            .addResource(new AuthorResource(authorService))
            .build();

    private Author testAuthor;

    @BeforeEach
    void setUp() {
        testAuthor = new Author(1, "Jane Austen", new Date(), "British");
    }

    @AfterEach
    void tearDown() {
        reset(authorService);
    }

    @Test
    void testCreateAuthor() {
        when(authorService.createAuthor(Mockito.any(Author.class))).thenReturn(testAuthor);

        Response response = resources.target("/authors")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(testAuthor, MediaType.APPLICATION_JSON));

        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.CREATED.getStatusCode());
        assertThat(response.readEntity(Author.class)).isEqualTo(testAuthor);
    }

    @Test
    void testGetAuthorByIdSuccess() {
        when(authorService.getAuthorById(1)).thenReturn(Optional.of(testAuthor));

        Author author = resources.target("/authors/1").request().get(Author.class);

        assertThat(author.getId()).isEqualTo(testAuthor.getId());
    }

    @Test
    void testGetAuthorByIdNotFound() {
        when(authorService.getAuthorById(2)).thenReturn(Optional.empty());

        Response response = resources.target("/authors/2").request().get();

        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void testGetAllAuthors() {
        // Correctly mock the method with pagination parameters
        when(authorService.getAllAuthors(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(Collections.singletonList(testAuthor));

        List<Author> authors = resources.target("/authors").request().get(new GenericType<List<Author>>() {});

        assertThat(authors).isNotEmpty();
    }

    @Test
    void testUpdateAuthorSuccess() {
        when(authorService.getAuthorById(1)).thenReturn(Optional.of(testAuthor));
        Author updatedAuthor = new Author(1, "Jane M. Austen", new Date(), "British");
        Mockito.doNothing().when(authorService).updateAuthor(updatedAuthor);

        Response response = resources.target("/authors/1")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(updatedAuthor, MediaType.APPLICATION_JSON));

        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    @Test
    void testUpdateAuthorNotFound() {
        when(authorService.getAuthorById(2)).thenReturn(Optional.empty());

        Response response = resources.target("/authors/2")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(new Author(), MediaType.APPLICATION_JSON));

        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void testDeleteAuthorSuccess() {
        when(authorService.getAuthorById(1)).thenReturn(Optional.of(testAuthor));

        Response response = resources.target("/authors/1").request().delete();

        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    void testDeleteAuthorNotFound() {
        when(authorService.getAuthorById(2)).thenReturn(Optional.empty());

        Response response = resources.target("/authors/2").request().delete();

        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }
}
