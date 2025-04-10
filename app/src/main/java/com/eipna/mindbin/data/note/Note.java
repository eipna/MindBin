package com.eipna.mindbin.data.note;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Comparator;

public class Note implements Parcelable {

    private String UUID;
    private String folderUUID;
    private String title;
    private String content;
    private long dateCreated;
    private int state;

    public static String NO_FOLDER = "undefined";

    public Note() {
        this.UUID = null;
        this.folderUUID = null;
        this.title = null;
        this.content = null;
        this.dateCreated = -1;
        this.state = -2;
    }

    public String getFolderUUID() {
        return folderUUID;
    }

    public void setFolderUUID(String folderUUID) {
        this.folderUUID = folderUUID;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public static final Comparator<Note> SORT_TITLE = Comparator.comparing(firstNote -> firstNote.getTitle().toLowerCase());

    public static final Comparator<Note> SORT_DATE_CREATED = Comparator.comparingLong(Note::getDateCreated);


    protected Note(Parcel in) {
        UUID = in.readString();
        folderUUID = in.readString();
        title = in.readString();
        content = in.readString();
        dateCreated = in.readLong();
        state = in.readInt();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel parcel) {
            return new Note(parcel);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel destination, int flags) {
        destination.writeString(UUID);
        destination.writeString(folderUUID);
        destination.writeString(title);
        destination.writeString(content);
        destination.writeLong(dateCreated);
        destination.writeInt(state);
    }
}