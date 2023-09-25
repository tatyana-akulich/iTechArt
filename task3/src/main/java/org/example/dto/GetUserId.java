package org.example.dto;

import java.util.List;

public class GetUserId {
    String userId;
    String username;
    List<Book> books;

    public String getUsername() {
        return username;
    }

    public List<Book> getBooks() {
        return books;
    }

    @Override
    public String toString() {
        return "GetUserId{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", books=" + books +
                '}';
    }
}
