package com.eipna.mindbin.data.note;

public class Note {

    private long ID;
    private String title;
    private String content;

    public Note() {
        this.ID = -1;
        this.title = null;
        this.content = null;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
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