package org.example.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.Objects;

public class Author {

    private long id;
    private String authorName;
    private Date birthDate;
    private String nationality;

    public Author() {
        // Required for Jackson deserialization
    }

    public Author(long id, String authorName, Date birthDate, String nationality) {
        this.id = id;
        this.authorName = authorName;
        this.birthDate = birthDate;
        this.nationality = nationality;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty("authorName") // This annotation helps Jackson serialize and deserialize the field
    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @JsonProperty("birthDate")
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @JsonProperty("nationality")
    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return id == author.id &&
                Objects.equals(authorName, author.authorName) &&
                Objects.equals(birthDate, author.birthDate) &&
                Objects.equals(nationality, author.nationality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authorName, birthDate, nationality);
    }
}