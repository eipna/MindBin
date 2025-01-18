package com.eipna.mindbin.data.note;

public enum NoteState {
    NORMAL(1),
    ARCHIVE(2),
    TRASH(-1);

    public static final NoteState[] states;
    public final int value;

    static {
        states = values();
    }

    NoteState(int value) {
        this.value = value;
    }
}