package com.serbi.mindbin.models;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class NoteModel {

    private int id;
    private String isFavorite;
    private String title;
    private String content;
    private String status;
    private String dateCreation;
    private String originalDateCreation;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

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

    public static Comparator<NoteModel> sortDateCreationByNewest = new Comparator<NoteModel>() {
        @Override
        public int compare(NoteModel note01, NoteModel note02) {
            try {
                Date date01 = dateFormat.parse(note01.getOriginalDateCreation());
                Date date02 = dateFormat.parse(note02.getOriginalDateCreation());
                return date01.compareTo(date02);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
    };

    public static Comparator<NoteModel> sortDateCreationByOldest = new Comparator<NoteModel>() {
        @Override
        public int compare(NoteModel note01, NoteModel note02) {
            try {
                Date date01 = dateFormat.parse(note01.getOriginalDateCreation());
                Date date02 = dateFormat.parse(note02.getOriginalDateCreation());
                return date02.compareTo(date01);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
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

    public String getOriginalDateCreation() {
        return originalDateCreation;
    }

    public void setOriginalDateCreation(String string) {
        this.originalDateCreation = string;
    }
}