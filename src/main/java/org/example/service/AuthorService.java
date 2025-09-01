package org.example.service;

import org.example.dao.AuthorDAO;
import org.example.bean.Author;

import java.util.List;
import java.util.Optional;

/**
 * Service class for handling business logic related to Authors.
 * It uses the AuthorDAO to interact with the database.
 */
public class AuthorService {

    private final AuthorDAO authorDAO;

    public AuthorService(AuthorDAO authorDAO) {
        this.authorDAO = authorDAO;
    }

    /**
     * Creates a new Author in the database.
     * @param author The Author object to create.
     * @return The created Author object with its new ID.
     */
    public Author createAuthor(Author author) {
        int newId = authorDAO.insert(author);
        author.setId(newId);
        return author;
    }

    /**
     * Retrieves an Author by their ID.
     * @param id The ID of the author to retrieve.
     * @return An Optional containing the found Author, or empty if not found.
     */
    public Optional<Author> getAuthorById(int id) {
        return authorDAO.findById(id);
    }

    /**
     * Retrieves all Authors with pagination.
     * @param limit The maximum number of results to return.
     * @param offset The offset for pagination.
     * @return A list of Author objects.
     */
    public List<Author> getAllAuthors(int limit, int offset) {
        return authorDAO.findAll(limit, offset);
    }

    /**
     * Searches for authors by name.
     * @param name The name to search for (supports wildcard).
     * @return A list of matching Author objects.
     */
    public List<Author> searchAuthorsByName(String name) {
        return authorDAO.findByName("%" + name + "%");
    }

    /**
     * Updates an existing Author's details.
     * @param author The Author object with updated details.
     */
    public void updateAuthor(Author author) {
        authorDAO.update(author);
    }

    /**
     * Deletes an Author by their ID.
     * @param id The ID of the author to delete.
     */
    public void deleteAuthorById(int id) {
        authorDAO.deleteById(id);
    }
}