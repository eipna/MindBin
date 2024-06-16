package com.serbi.mindbin.models;

public class Note {

    private String title;
    private String content;
    private String status;
    private String dateCreation;

    public Note(String title, String dateCreation, String status, String content) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.dateCreation = dateCreation;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getStatus() {
        return status;
    }

    public String getDateCreation() {
        return dateCreation;
    }
}