package org.example.dao;

import org.example.bean.Author;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RegisterRowMapper(AuthorDAO.AuthorMapper.class)
public interface AuthorDAO {

    @SqlUpdate("INSERT INTO authors (authorName, birthDate, nationality) VALUES (:authorName, :birthDate, :nationality)")
    @GetGeneratedKeys
    int insert(@BindBean Author author);

    @SqlQuery("SELECT * FROM authors LIMIT :limit OFFSET :offset")
    List<Author> findAll(@Bind("limit") int limit, @Bind("offset") int offset);

    @SqlQuery("SELECT * FROM authors WHERE id = :id")
    Optional<Author> findById(@Bind("id") int id);

    @SqlQuery("SELECT * FROM authors WHERE authorName LIKE :name")
    List<Author> findByName(@Bind("name") String name);

    @SqlUpdate("UPDATE authors SET authorName = :authorName, birthDate = :birthDate, nationality = :nationality WHERE id = :id")
    void update(@BindBean Author author);

    @SqlUpdate("DELETE FROM authors WHERE id = :id")
    void deleteById(@Bind("id") int id);

    class AuthorMapper implements RowMapper<Author> {
        @Override
        public Author map(ResultSet rs, StatementContext ctx) throws SQLException {
            Date birthDate = rs.getDate("birthDate");
            return new Author(
                    rs.getLong("id"),
                    rs.getString("authorName"),
                    birthDate != null ? new Date(birthDate.getTime()) : null,
                    rs.getString("nationality")
            );
        }
    }
}