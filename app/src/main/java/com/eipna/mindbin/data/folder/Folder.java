package com.eipna.mindbin.data.folder;

public class Folder {

    public String UUID;
    public String name;
    public String description;
    public int isPinned;

    public static final int IS_PINNED = 1;
    public static final int NOT_PINNED = 0;

    public Folder() {
        this.UUID = null;
        this.name = null;
        this.description = null;
        this.isPinned = NOT_PINNED;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsPinned() {
        return isPinned;
    }

    public void setIsPinned(int isPinned) {
        this.isPinned = isPinned;
    }
}