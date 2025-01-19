package com.eipna.mindbin.data.note;

public class Note {

    private int ID;
    private String title;
    private String content;
    private long dateCreated;
    private long lastUpdated;

    public Note() {
        this.ID = -1;
        this.title = null;
        this.content = null;
        this.dateCreated = -1;
        this.lastUpdated = -1;
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

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}