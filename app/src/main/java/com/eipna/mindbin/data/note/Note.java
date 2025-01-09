package com.eipna.mindbin.data.note;

public class Note {

    private int ID;
    private String title;
    private String content;

    public Note() {
        this.ID = -1;
        this.title = null;
        this.content = null;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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
}