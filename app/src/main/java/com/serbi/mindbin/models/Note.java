package com.serbi.mindbin.models;

public class Note {

    private int id;
    private String title;
    private String content;
    private String status;
    private String dateCreation;

    public Note(int id, String title, String dateCreation, String status, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.status = status;
        this.dateCreation = dateCreation;
    }

    public int getId() {
        return id;
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