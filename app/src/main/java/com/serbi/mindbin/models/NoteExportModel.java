package com.serbi.mindbin.models;

public class NoteExportModel {
    private String isFavorite;
    private String title;
    private String content;
    private String status;
    private String dateCreation;

    public NoteExportModel(String title, String dateCreation, String status, String content, String isFavorite) {
        this.isFavorite = isFavorite;
        this.title = title;
        this.content = content;
        this.status = status;
        this.dateCreation = dateCreation;
    }

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }
}