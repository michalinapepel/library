package domain;

import java.util.ArrayList;
import java.util.List;

public class Book {

    private int id;
    private Integer shelfId;
    private String title = "";
    private Author[] authors = null;
    private String publisher = "";
    private int yearOfPublishing = 0;
    private String isbn = "";
    private List<Section> sections = new ArrayList<>();

    public Book() {}

    public Book(String title, String publisher, int publicationYear, String isbn, Integer shelfId) {
        this.title = title;
        this.publisher = publisher;
        this.yearOfPublishing = publicationYear;
        this.isbn = isbn;
        this.shelfId = shelfId;
    }

    public Book(int id, String title, String publisher, int publicationYear, String isbn, Integer shelfId) {
        this.id = id;
        this.title = title;
        this.publisher = publisher;
        this.yearOfPublishing = publicationYear;
        this.isbn = isbn;
        this.shelfId = shelfId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public int getPublicationYear() { return yearOfPublishing; }
    public void setYearOfPublishing(Integer value) { this.yearOfPublishing = value; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getShelfId() { return shelfId; }
    public void setShelfId(Integer shelfId) { this.shelfId = shelfId; }

    public Author[] getAuthors() { return authors; }
    public void setAuthors(Author[] authors) { this.authors = authors; }

    public String getAuthorsAsString() {
        StringBuilder result = new StringBuilder();
        if (authors != null) {
            for (Author author : authors) {
                result.append(author.getFirstName()).append(" ").append(author.getLastName()).append("; ");
            }
        }
        if (result.length() > 2) result.setLength(result.length() - 2);
        return result.toString();
    }

    public List<Section> getSections() { return sections; }
    public void setSections(List<Section> sections) { this.sections = sections != null ? sections : new ArrayList<>(); }

    public String getSectionsAsString() {
        StringBuilder sb = new StringBuilder();
        for (Section section : sections) {
            sb.append(section.getName()).append(", ");
        }
        if (sb.length() > 2) sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    @Override
    public String toString() { return title; }
}
