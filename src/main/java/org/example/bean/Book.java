package org.example.bean;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.Objects;

public class Book {

    private long id;
    private String title;
    private long authorId;
    private Date publishDate;
    private String isbn;

    public Book() {
        // Required for Jackson deserialization
    }

    public Book(long id, String title, long authorId, Date publishDate, String isbn) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.publishDate = publishDate;
        this.isbn = isbn;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    @JsonProperty
    public String getTitle() {
        return title;
    }

    @JsonProperty
    public long getAuthorId() {
        return authorId;
    }

    @JsonProperty
    public Date getPublishDate() {
        return publishDate;
    }

    @JsonProperty
    public String getIsbn() {
        return isbn;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id &&
                authorId == book.authorId &&
                Objects.equals(title, book.title) &&
                Objects.equals(publishDate, book.publishDate) &&
                Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, authorId, publishDate, isbn);
    }
}
