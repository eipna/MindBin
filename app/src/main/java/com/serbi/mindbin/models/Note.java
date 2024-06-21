package com.serbi.mindbin.models;

import java.util.Comparator;

public class Note {

    private int id;
    private String isFavorite;
    private String title;
    private String content;
    private String status;
    private String dateCreation;

    public Note(int id, String title, String dateCreation, String status, String content, String isFavorite) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.status = status;
        this.dateCreation = dateCreation;
        this.isFavorite = isFavorite;
    }

    public static Comparator<Note> sortByAsc = new Comparator<Note>() {
        @Override
        public int compare(Note note1, Note note2) {
            return note1.getTitle().compareTo(note2.getTitle());
        }
    };

    public static Comparator<Note> sortByDesc = new Comparator<Note>() {
        @Override
        public int compare(Note note1, Note note2) {
            return note2.getTitle().compareTo(note1.getTitle());
        }
    };

    public int getId() {
        return id;
    }

    public String isFavorite() {
        return isFavorite;
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