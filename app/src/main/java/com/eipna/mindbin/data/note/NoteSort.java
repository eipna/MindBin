package com.eipna.mindbin.data.note;

public enum NoteSort {

    TITLE("sort_title"),
    DATE_CREATED("sort_date_created"),
    LAST_UPDATED("sort_last_updated"),
    ASCENDING("sort_ascending"),
    DESCENDING("sort_descending");

    public static final NoteSort[] sorts;
    public final String SORT;

    static {
        sorts = values();
    }

    NoteSort(String sort) {
        this.SORT = sort;
    }
}