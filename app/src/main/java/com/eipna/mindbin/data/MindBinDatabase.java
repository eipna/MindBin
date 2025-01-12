package com.eipna.mindbin.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.eipna.mindbin.data.note.Note;
import com.eipna.mindbin.data.note.NoteRepository;

import java.util.ArrayList;

public class MindBinDatabase extends SQLiteOpenHelper implements NoteRepository {

    private static final String DATABASE_NAME = "mindbin.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NOTE = "notes";
    public static final String COLUMN_NOTE_ID = "note_id";
    public static final String COLUMN_NOTE_TITLE = "title";
    public static final String COLUMN_NOTE_CONTENT = "content";

    public MindBinDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createNoteTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTE + "(" +
                COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOTE_TITLE + " TEXT, " +
                COLUMN_NOTE_CONTENT + " TEXT)";
        sqLiteDatabase.execSQL(createNoteTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void createNote(Note createdNote) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, createdNote.getTitle());
        values.put(COLUMN_NOTE_CONTENT, createdNote.getContent());
        database.insert(TABLE_NOTE, null, values);
        database.close();
    }

    @Override
    public void updateNote(Note updatedNote) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, updatedNote.getTitle());
        values.put(COLUMN_NOTE_CONTENT, updatedNote.getContent());
        database.update(TABLE_NOTE, values, COLUMN_NOTE_ID + " = ?", new String[]{String.valueOf(updatedNote.getID())});
        database.close();
    }

    @Override
    public void deleteNote(Note deletedNote) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_NOTE, COLUMN_NOTE_ID + " = ?", new String[]{String.valueOf(deletedNote.getID())});
        database.close();
    }

    @SuppressLint("Range")
    @Override
    public ArrayList<Note> getNotes() {
        SQLiteDatabase database = getReadableDatabase();
        ArrayList<Note> list = new ArrayList<>();
        String readNotesQuery = "SELECT * FROM " + TABLE_NOTE;

        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery(readNotesQuery, null);
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