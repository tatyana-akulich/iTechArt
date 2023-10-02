package by.itechart.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Builder
public class Book {
    String isbn;
    String title;
    String subTitle;
    String author;
    String publish_date;
    String publisher;
    int pages;
    String description;
    String website;

    public void setPages(int pages) {
        this.pages = pages;
    }

    @Override
    public String toString() {
        return "{\"isbn\":" + "\"" + isbn + "\", " +
                "\"title\":" + "\"" + title + "\", " +
                "\"subTitle\":" + "\"" + subTitle + "\", " +
                "\"author\":" + "\"" + author + "\", " +
                "\"publish_date\":" + "\"" + publish_date + "\", " +
                "\"publisher\":" + "\"" + publisher + "\", " +
                "\"pages\":" + "\"" + pages + "\", " +
                "\"description\":" + "\"" + description + "\", " +
                "\"website\":" + "\"" + website + "\"}";
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(title, book.title) && Objects.equals(author, book.author) && Objects.equals(publisher, book.publisher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, publisher);
    }
}
