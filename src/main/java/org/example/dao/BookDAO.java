package org.example.dao;

import org.example.bean.Book;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import java.util.List;
import java.util.Optional;

@RegisterBeanMapper(Book.class)
public interface BookDAO {

    @SqlUpdate("INSERT INTO books (title, authorId, publishDate, isbn) VALUES (:title, :authorId, :publishDate, :isbn)")
    @GetGeneratedKeys
    long create(@BindBean Book book);

    @SqlQuery("SELECT * FROM books WHERE id = :id")
    Optional<Book> findById(@Bind("id") long id);

    // --- Pagination implemented ---
    @SqlQuery("SELECT * FROM books LIMIT :limit OFFSET :offset")
    List<Book> findAll(@Bind("limit") int limit, @Bind("offset") int offset);
    // ----------------------------

    // --- Search functionality added ---
    @SqlQuery("SELECT * FROM books WHERE title LIKE :title")
    List<Book> findByTitle(@Bind("title") String title);
    // --------------------------------

    // --- Corrected Update Method ---
    @SqlUpdate("UPDATE books SET title = :title, authorId = :authorId, publishDate = :publishDate, isbn = :isbn WHERE id = :id")
    void update(@BindBean Book book);
    // --------------------------------

    @SqlUpdate("DELETE FROM books WHERE id = :id")
    void delete(@Bind("id") long id);
}