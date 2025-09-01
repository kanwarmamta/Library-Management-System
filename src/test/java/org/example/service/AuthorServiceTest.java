package org.example.service;

import org.example.bean.Author;
import org.example.dao.AuthorDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorDAO authorDAO;

    @InjectMocks
    private AuthorService authorService;

    private Author testAuthor;

    @BeforeEach
    void setUp() {
        testAuthor = new Author(1, "Jane Austen", new Date(), "British");
    }

    @Test
    void testCreateAuthor() {
        when(authorDAO.insert(any(Author.class))).thenReturn(1);
        Author newAuthor = new Author();
        newAuthor.setAuthorName("Jane Austen");
        newAuthor.setBirthDate(new Date());
        newAuthor.setNationality("British");

        Author createdAuthor = authorService.createAuthor(newAuthor);

        assertEquals(1, createdAuthor.getId());
        verify(authorDAO).insert(newAuthor);
    }

    @Test
    void testGetAuthorById() {
        when(authorDAO.findById(1)).thenReturn(Optional.of(testAuthor));
        Optional<Author> foundAuthor = authorService.getAuthorById(1);

        assertTrue(foundAuthor.isPresent());
        assertEquals(testAuthor, foundAuthor.get());
        verify(authorDAO).findById(1);
    }

    @Test
    void testGetAllAuthors() {
        List<Author> authors = Collections.singletonList(testAuthor);
        when(authorDAO.findAll(20, 0)).thenReturn(authors);
        List<Author> foundAuthors = authorService.getAllAuthors(20, 0);

        assertEquals(1, foundAuthors.size());
        assertEquals(testAuthor, foundAuthors.get(0));
        verify(authorDAO).findAll(20, 0);
    }

    @Test
    void testSearchAuthorsByName() {
        List<Author> authors = Collections.singletonList(testAuthor);
        when(authorDAO.findByName("%Austen%")).thenReturn(authors);
        List<Author> foundAuthors = authorService.searchAuthorsByName("Austen");

        assertEquals(1, foundAuthors.size());
        verify(authorDAO).findByName("%Austen%");
    }

    @Test
    void testUpdateAuthor() {
        authorService.updateAuthor(testAuthor);
        verify(authorDAO).update(testAuthor);
    }

    @Test
    void testDeleteAuthorById() {
        authorService.deleteAuthorById(1);
        verify(authorDAO).deleteById(1);
    }
}