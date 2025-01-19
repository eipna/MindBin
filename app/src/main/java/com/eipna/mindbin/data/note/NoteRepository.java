package com.eipna.mindbin.data.note;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.eipna.mindbin.data.MindBinDatabase;

import java.util.ArrayList;
import java.util.List;

public class NoteRepository extends MindBinDatabase {
    public NoteRepository(@Nullable Context context) {
        super(context);
    }

    public void create(Note createdNote) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, createdNote.getTitle());
        values.put(COLUMN_NOTE_CONTENT, createdNote.getContent());
        values.put(COLUMN_NOTE_DATE_CREATED, createdNote.getDateCreated());
        values.put(COLUMN_NOTE_LAST_UPDATED, createdNote.getLastUpdated());
        database.insert(TABLE_NOTE, null, values);
        database.close();
    }

    public void update(Note updatedNote) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, updatedNote.getTitle());
        values.put(COLUMN_NOTE_CONTENT, updatedNote.getContent());
        values.put(COLUMN_NOTE_LAST_UPDATED, updatedNote.getLastUpdated());
        database.update(TABLE_NOTE, values, COLUMN_NOTE_ID + " = ?", new String[]{String.valueOf(updatedNote.getID())});
        database.close();
    }

    public void delete(Note deletedNote) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_NOTE, COLUMN_NOTE_ID + " = ?", new String[]{String.valueOf(deletedNote.getID())});
        database.close();
    }

    @SuppressLint("Range")
    public List<Note> getNotes() {
        SQLiteDatabase database = getReadableDatabase();
        List<Note> list = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NOTE;

        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Note queriedNote = new Note();
                queriedNote.setID(cursor.getInt(cursor.getColumnIndex(COLUMN_NOTE_ID)));
                queriedNote.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_TITLE)));
                queriedNote.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_CONTENT)));
                list.add(queriedNote);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return list;
    }
}