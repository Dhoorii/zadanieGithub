package com.ryer.zadaniegithub.api;

import java.sql.Date;

public class Commit {
    private String message;
    private Author author;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
