package com.eipna.mindbin.data.note;

import java.util.ArrayList;

public interface NoteRepository {
    void createNote(Note createdNote);
    void updateNote(Note updatedNote);
    void deleteNote(Note deletedNote);
    ArrayList<Note> getNotes();
}