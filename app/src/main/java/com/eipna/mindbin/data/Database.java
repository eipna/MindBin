package com.eipna.mindbin.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mindbin.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NOTE = "notes";
    public static final String COLUMN_NOTE_ID = "note_id";
    public static final String COLUMN_NOTE_TITLE = "title";
    public static final String COLUMN_NOTE_CONTENT = "content";
    public static final String COLUMN_NOTE_DATE_CREATED = "date_created";
    public static final String COLUMN_NOTE_STATE = "state";

    public static final String TABLE_FOLDER = "folders";
    public static final String COLUMN_FOLDER_ID = "folder_id";
    public static final String COLUMN_FOLDER_PINNED = "is_pinned";
    public static final String COLUMN_FOLDER_NAME = "name";
    public static final String COLUMN_FOLDER_DESCRIPTION = "description";

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createNoteTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTE + "(" +
                COLUMN_NOTE_ID + " TEXT PRIMARY KEY, " +
                COLUMN_NOTE_TITLE + " TEXT, " +
                COLUMN_NOTE_CONTENT + " TEXT, " +
                COLUMN_NOTE_DATE_CREATED + " INTEGER NOT NULL, " +
                COLUMN_NOTE_STATE + " INTEGER NOT NULL)";

        String createFolderTable = "CREATE TABLE IF NOT EXISTS " + TABLE_FOLDER + "(" +
                COLUMN_FOLDER_ID + " TEXT PRIMARY KEY, " +
                COLUMN_FOLDER_PINNED + " INTEGER NOT NULL, " +
                COLUMN_FOLDER_NAME + " TEXT NOT NULL UNIQUE, " +
                COLUMN_FOLDER_DESCRIPTION + " TEXT);";

        sqLiteDatabase.execSQL(createNoteTable);
        sqLiteDatabase.execSQL(createFolderTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FOLDER);
        onCreate(sqLiteDatabase);
    }
}