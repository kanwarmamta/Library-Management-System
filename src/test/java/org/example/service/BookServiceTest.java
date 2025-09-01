package org.example.service;

import org.example.bean.Book;
import org.example.dao.BookDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
class BookServiceTest {

    @Mock
    private BookDAO bookDAO;

    @InjectMocks
    private BookService bookService;

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = new Book(1, "The Hobbit", 1L, new Date(), "12345");
    }

    @Test
    void testCreateBook() {
        when(bookDAO.create(any(Book.class))).thenReturn(1L);
        Book newBook = new Book();
        newBook.setTitle("The Hobbit");
        newBook.setAuthorId(1L);
        newBook.setPublishDate(new Date());
        newBook.setIsbn("12345");

        Book createdBook = bookService.createBook(newBook);

        assertEquals(1, createdBook.getId());
        verify(bookDAO).create(newBook);
    }

    @Test
    void testGetBookById() {
        when(bookDAO.findById(1)).thenReturn(Optional.of(testBook));
        Optional<Book> foundBook = bookService.getBookById(1);

        assertTrue(foundBook.isPresent());
        assertEquals(testBook, foundBook.get());
        verify(bookDAO).findById(1);
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = Collections.singletonList(testBook);
        when(bookDAO.findAll(20, 0)).thenReturn(books);
        List<Book> foundBooks = bookService.getAllBooks(20, 0);

        assertEquals(1, foundBooks.size());
        assertEquals(testBook, foundBooks.get(0));
        verify(bookDAO).findAll(20, 0);
    }

    @Test
    void testSearchBooksByTitle() {
        List<Book> books = Collections.singletonList(testBook);
        when(bookDAO.findByTitle("%Hobbit%")).thenReturn(books);
        List<Book> foundBooks = bookService.searchBooksByTitle("Hobbit");

        assertEquals(1, foundBooks.size());
        verify(bookDAO).findByTitle("%Hobbit%");
    }

    @Test
    void testUpdateBook() {
        bookService.updateBook(testBook);
        verify(bookDAO).update(testBook);
    }

    @Test
    void testDeleteBookById() {
        bookService.deleteBookById(1);
        verify(bookDAO).delete(1);
    }
}
