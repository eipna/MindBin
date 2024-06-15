package com.serbi.mindbin.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static String DATABASE_NAME = "mindbin.db";
    private static int DATABASE_VERSION = 1;

    private static String TABLE_USER = "user";
    private static String TABLE_USER_COL_NAME = "name";
    private static String TABLE_USER_COL_PROFILE = "profile";

    private static String TABLE_NOTE = "notes";
    private static String TABLE_NOTE_COL_ID = "note_id";
    private static String TABLE_NOTE_COL_TITLE = "title";
    private static String TABLE_NOTE_COL_DATE  = "date_created";
    private static String TABLE_NOTE_COL_STATUS = "status";
    private static String TABLE_NOTE_COL_CONTENT = "content";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE " + TABLE_USER + "(" +
                TABLE_USER_COL_NAME + " TEXT, " + TABLE_USER_COL_PROFILE + " BLOB)";

        String createNotesTable = "CREATE TABLE " + TABLE_NOTE + "(" +
                TABLE_NOTE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_NOTE_COL_TITLE + " TEXT, " + TABLE_NOTE_COL_DATE +
                " DATE, " + TABLE_NOTE_COL_STATUS + " TEXT, " + TABLE_NOTE_COL_CONTENT + " TEXT)";

        db.execSQL(createUserTable);
        db.execSQL(createNotesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_USER);
        db.execSQL("DROP TABLE " + TABLE_NOTE);
        onCreate(db);
    }

    public void createNote(String title, String content, String status, String dateCreation) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TABLE_NOTE_COL_TITLE, title);
        values.put(TABLE_NOTE_COL_CONTENT, content);
        values.put(TABLE_NOTE_COL_STATUS, status);
        values.put(TABLE_NOTE_COL_DATE, dateCreation);

        long result = database.insert(TABLE_NOTE, null, values);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
        }
    }
}