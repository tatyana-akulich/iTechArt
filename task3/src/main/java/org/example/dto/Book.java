package org.example.dto;

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
}
