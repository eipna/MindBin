package com.eipna.mindbin.data.note;

import java.util.Comparator;

public enum NoteSort {
    TITLE_ASCENDING("title_ascending", Note.SORT_TITLE_ASCENDING),
    TITLE_DESCENDING("title_descending", Note.SORT_TITLE_DESCENDING),
    DATE_CREATED_LATEST("date_created_latest", Note.SORT_DATE_CREATED_LATEST),
    DATE_CREATED_OLDEST("date_created_oldest", Note.SORT_DATE_CREATED_OLDEST),
    LAST_UPDATED_LATEST("last_updated_latest", Note.SORT_LAST_UPDATED_LATEST),
    LAST_UPDATES_OLDEST("last_updated_oldest", Note.SORT_LAST_UPDATED_OLDEST);

    public static final NoteSort[] sorts;
    public final String NAME;
    public final Comparator<Note> ORDER;

    static {
        sorts = values();
    }

    NoteSort(String name, Comparator<Note> order) {
        this.NAME = name;
        this.ORDER = order;
    }
}