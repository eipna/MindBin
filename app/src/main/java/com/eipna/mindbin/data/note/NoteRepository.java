package com.eipna.mindbin.data.note;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.eipna.mindbin.data.Database;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NoteRepository {

    private final Database instance;

    public NoteRepository(@NotNull Context context) {
        this.instance = new Database(context);
    }

    public void create(Note note) {
        SQLiteDatabase database  = instance.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Database.COLUMN_NOTE_TITLE, note.getTitle());
        values.put(Database.COLUMN_NOTE_CONTENT, note.getContent());
        database.insert(Database.TABLE_NOTE, null, values);
        database.close();
    }

    @SuppressLint("Range")
    public ArrayList<Note> getNotes() {
        ArrayList<Note> list = new ArrayList<>();
        String query = "SELECT * FROM " + Database.TABLE_NOTE;
        SQLiteDatabase database = instance.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setID(cursor.getInt(cursor.getColumnIndex(Database.COLUMN_NOTE_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(Database.COLUMN_NOTE_TITLE)));
                note.setContent(cursor.getString(cursor.getColumnIndex(Database.COLUMN_NOTE_CONTENT)));
                list.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return list;
    }

    public void update(Note note) {
        SQLiteDatabase database = instance.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Database.COLUMN_NOTE_TITLE, note.getTitle());
        values.put(Database.COLUMN_NOTE_CONTENT, note.getContent());
        database.update(Database.TABLE_NOTE, values, Database.COLUMN_NOTE_ID + "=?", new String[]{String.valueOf(note.getID())});
        database.close();
    }

    public void delete(Note note) {
        SQLiteDatabase database = instance.getWritableDatabase();
        database.delete(Database.TABLE_NOTE, Database.COLUMN_NOTE_ID + "=?", new String[]{String.valueOf(note.getID())});
        database.close();
    }
}