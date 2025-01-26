package com.eipna.mindbin.data.note;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Comparator;

public class Note implements Parcelable {

    private int ID;
    private String title;
    private String content;
    private long dateCreated;
    private long lastUpdated;
    private int state;

    public Note() {
        this.ID = -1;
        this.title = null;
        this.content = null;
        this.dateCreated = -1;
        this.lastUpdated = -1;
        this.state = -2;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public static final Comparator<Note> SORT_TITLE_ASCENDING = (firstNote, secondNote) -> firstNote.getTitle().compareToIgnoreCase(secondNote.getTitle());

    public static final Comparator<Note> SORT_TITLE_DESCENDING = (firstNote, secondNote) -> secondNote.getTitle().compareToIgnoreCase(firstNote.getTitle());

    public static final Comparator<Note> SORT_DATE_CREATED_OLDEST = Comparator.comparingLong(Note::getDateCreated);

    public static final Comparator<Note> SORT_DATE_CREATED_LATEST = (firstNote, secondNote) -> Long.compare(secondNote.getDateCreated(), firstNote.getDateCreated());

    public static final Comparator<Note> SORT_LAST_UPDATED_OLDEST = Comparator.comparingLong(Note::getLastUpdated);

    public static final Comparator<Note> SORT_LAST_UPDATED_LATEST = (firstNote, secondNote) -> Long.compare(secondNote.getLastUpdated(), firstNote.getLastUpdated());

    protected Note(Parcel in) {
        ID = in.readInt();
        title = in.readString();
        content = in.readString();
        dateCreated = in.readLong();
        lastUpdated = in.readLong();
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
        destination.writeInt(ID);
        destination.writeString(title);
        destination.writeString(content);
        destination.writeLong(dateCreated);
        destination.writeLong(lastUpdated);
        destination.writeInt(state);
    }
}