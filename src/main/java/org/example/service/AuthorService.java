package org.example.service;

import org.example.dao.AuthorDAO;
import org.example.bean.Author;

import java.util.List;
import java.util.Optional;

public class AuthorService {

    private final AuthorDAO authorDAO;

    public AuthorService(AuthorDAO authorDAO) {
        this.authorDAO = authorDAO;
    }

    public Author createAuthor(Author author) {
        int newId = authorDAO.insert(author);
        author.setId(newId);
        return author;
    }

    public Optional<Author> getAuthorById(int id) {
        return authorDAO.findById(id);
    }

    public List<Author> getAllAuthors(int limit, int offset) {
        return authorDAO.findAll(limit, offset);
    }

    public List<Author> searchAuthorsByName(String name) {
        return authorDAO.findByName("%" + name + "%");
    }

    public void updateAuthor(Author author) {
        authorDAO.update(author);
    }

    public void deleteAuthorById(int id) {
        authorDAO.deleteById(id);
    }
}