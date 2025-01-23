package com.eipna.mindbin.data.note;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.eipna.mindbin.data.MindBinDatabase;

import java.util.ArrayList;

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
        values.put(COLUMN_NOTE_STATE, createdNote.getState());
        database.insert(TABLE_NOTE, null, values);
        database.close();
    }

    public void update(Note updatedNote) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, updatedNote.getTitle());
        values.put(COLUMN_NOTE_CONTENT, updatedNote.getContent());
        values.put(COLUMN_NOTE_LAST_UPDATED, updatedNote.getLastUpdated());
        values.put(COLUMN_NOTE_STATE, updatedNote.getState());
        database.update(TABLE_NOTE, values, COLUMN_NOTE_ID + " = ?", new String[]{String.valueOf(updatedNote.getID())});
        database.close();
    }

    public void updateState(int noteID, int updatedState) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_STATE, updatedState);
        database.update(TABLE_NOTE, values, COLUMN_NOTE_ID + " = ?" , new String[]{String.valueOf(noteID)});
        database.close();
    }

    public void delete(Note deletedNote) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_NOTE, COLUMN_NOTE_ID + " = ?", new String[]{String.valueOf(deletedNote.getID())});
        database.close();
    }

    @SuppressLint("Range")
    public ArrayList<Note> getAll() {
        SQLiteDatabase database = getReadableDatabase();
        ArrayList<Note> list = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NOTE;

        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Note queriedNote = new Note();
                queriedNote.setID(cursor.getInt(cursor.getColumnIndex(COLUMN_NOTE_ID)));
                queriedNote.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_TITLE)));
                queriedNote.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_CONTENT)));
                queriedNote.setDateCreated(cursor.getLong(cursor.getColumnIndex(COLUMN_NOTE_DATE_CREATED)));
                queriedNote.setLastUpdated(cursor.getLong(cursor.getColumnIndex(COLUMN_NOTE_LAST_UPDATED)));
                queriedNote.setState(cursor.getInt(cursor.getColumnIndex(COLUMN_NOTE_STATE)));
                list.add(queriedNote);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return list;
    }

    @SuppressLint("Range")
    public ArrayList<Note> getByState(NoteState state) {
        SQLiteDatabase database = getReadableDatabase();
        ArrayList<Note> list = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NOTE + " WHERE " + COLUMN_NOTE_STATE + " = ?";

        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(state.value)});
        if (cursor.moveToFirst()) {
            do {
                Note queriedNote = new Note();
                queriedNote.setID(cursor.getInt(cursor.getColumnIndex(COLUMN_NOTE_ID)));
                queriedNote.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_TITLE)));
                queriedNote.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_CONTENT)));
                queriedNote.setDateCreated(cursor.getLong(cursor.getColumnIndex(COLUMN_NOTE_DATE_CREATED)));
                queriedNote.setLastUpdated(cursor.getLong(cursor.getColumnIndex(COLUMN_NOTE_LAST_UPDATED)));
                queriedNote.setState(cursor.getInt(cursor.getColumnIndex(COLUMN_NOTE_STATE)));
                list.add(queriedNote);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return list;
    }

    public void clearByState(NoteState state) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_NOTE, COLUMN_NOTE_STATE + " = ?", new String[]{String.valueOf(state.value)});
        database.close();
    }
}