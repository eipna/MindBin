package com.serbi.mindbin.models;

import java.util.Comparator;

public class NoteModel {

    private int id;
    private String isFavorite;
    private String title;
    private String content;
    private String status;
    private String dateCreation;

    public NoteModel(int id, String title, String dateCreation, String status, String content, String isFavorite) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.status = status;
        this.dateCreation = dateCreation;
        this.isFavorite = isFavorite;
    }

    public static Comparator<NoteModel> sortTitleByAsc = new Comparator<NoteModel>() {
        @Override
        public int compare(NoteModel note1, NoteModel note2) {
            return note1.getTitle().compareTo(note2.getTitle());
        }
    };

    public static Comparator<NoteModel> sortTitleByDesc = new Comparator<NoteModel>() {
        @Override
        public int compare(NoteModel note1, NoteModel note2) {
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