package org.example.service;

import org.example.bean.Book;
import org.example.dao.BookDAO;
import java.util.List;
import java.util.Optional;

public class BookService {

    private final BookDAO bookDAO;

    public BookService(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    public Book createBook(Book book) {
        // Business logic, e.g., validating the authorId
        long newId = bookDAO.create(book);
        book.setId(newId);
        return book;
    }

    public Optional<Book> getBookById(long id) {
        return bookDAO.findById(id);
    }

    // Updated to support pagination
    public List<Book> getAllBooks(int limit, int offset) {
        return bookDAO.findAll(limit, offset);
    }

    // New method for searching books by title
    public List<Book> searchBooksByTitle(String title) {
        return bookDAO.findByTitle("%" + title + "%");
    }

    // Corrected method signature to accept a single Book object
    public void updateBook(Book updatedBook) {
        // The DAO handles the update based on the ID within the book object.
        bookDAO.update(updatedBook);
    }

    // Corrected method signature to match DAO
    public void deleteBookById(long id) {
        bookDAO.delete(id);
    }
}