package com.serbi.mindbin.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.serbi.mindbin.constants.NoteStatus;
import com.serbi.mindbin.models.NoteExportModel;
import com.serbi.mindbin.models.NoteModel;

import org.w3c.dom.Node;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final Context context;
    private static final String DATABASE_NAME = "mindbin.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USER = "user";
    private static final String TABLE_USER_COL_NAME = "name";
    private static final String TABLE_USER_COL_PROFILE = "profile";

    private static final String TABLE_NOTE = "notes";
    private static final String TABLE_NOTE_COL_ID = "note_id";
    private static final String TABLE_NOTE_COL_TITLE = "title";
    private static final String TABLE_NOTE_COL_DATE  = "date_created";
    private static final String TABLE_NOTE_COL_STATUS = "status";
    private static final String TABLE_NOTE_COL_CONTENT = "content";
    private static final String TABLE_NOTE_COL_isFAVORITE = "is_favorite";

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
                " DATE, " + TABLE_NOTE_COL_STATUS + " TEXT, " + TABLE_NOTE_COL_CONTENT + " TEXT, "
                + TABLE_NOTE_COL_isFAVORITE + " TEXT)";

        db.execSQL(createUserTable);
        db.execSQL(createNotesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_USER);
        db.execSQL("DROP TABLE " + TABLE_NOTE);
        onCreate(db);
    }

    public void createNote(String title, String content, String status, String dateCreation, String isFavorite) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TABLE_NOTE_COL_TITLE, title);
        values.put(TABLE_NOTE_COL_CONTENT, content);
        values.put(TABLE_NOTE_COL_STATUS, status);
        values.put(TABLE_NOTE_COL_DATE, dateCreation);
        values.put(TABLE_NOTE_COL_isFAVORITE, isFavorite);

        database.insert(TABLE_NOTE, null, values);
    }

    public void clearTrashNotes() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NOTE, TABLE_NOTE_COL_STATUS + " = ?", new String[]{NoteStatus.DELETED.toString()});
    }

    public void editNote(int id, String title, String content, String status) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TABLE_NOTE_COL_TITLE, title);
        values.put(TABLE_NOTE_COL_CONTENT, content);
        values.put(TABLE_NOTE_COL_STATUS, status);

        database.update(TABLE_NOTE, values, TABLE_NOTE_COL_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void updateNoteStatus(int id, NoteStatus status) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (status == NoteStatus.ARCHIVED) {
            values.put(TABLE_NOTE_COL_STATUS, NoteStatus.ARCHIVED.toString());
        }

        if (status == NoteStatus.DELETED) {
            values.put(TABLE_NOTE_COL_STATUS, NoteStatus.DELETED.toString());
        }

        database.update(TABLE_NOTE, values, TABLE_NOTE_COL_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void deleteNote(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NOTE, TABLE_NOTE_COL_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void revertNoteStatus(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TABLE_NOTE_COL_STATUS, NoteStatus.NORMAL.toString());
        database.update(TABLE_NOTE, values, TABLE_NOTE_COL_ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void addToFavorites(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TABLE_NOTE_COL_isFAVORITE, "yes");
        database.update(TABLE_NOTE, values, TABLE_NOTE_COL_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void removeFromFavorites(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TABLE_NOTE_COL_isFAVORITE, "no");
        database.update(TABLE_NOTE, values, TABLE_NOTE_COL_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public Cursor getFavoriteNotes() {
        String readFavoriteNotes = "SELECT * FROM " + TABLE_NOTE + " WHERE " + TABLE_NOTE_COL_isFAVORITE + " = ?"
                + " AND " + TABLE_NOTE_COL_STATUS + " = ?";
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery(readFavoriteNotes, new String[]{"yes", NoteStatus.NORMAL.toString()});
    }

    public Cursor getNormalNotes() {
        String readNormalNotes = "SELECT * FROM " + TABLE_NOTE + " WHERE " + TABLE_NOTE_COL_STATUS + " = ?";
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery(readNormalNotes, new String[]{NoteStatus.NORMAL.toString()});
    }

    public Cursor getArchivedNotes() {
        String readNormalNotes = "SELECT * FROM " + TABLE_NOTE + " WHERE " + TABLE_NOTE_COL_STATUS + " = ?";
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery(readNormalNotes, new String[]{NoteStatus.ARCHIVED.toString()});
    }

    public Cursor getDeletedNotes() {
        String readNormalNotes = "SELECT * FROM " + TABLE_NOTE + " WHERE " + TABLE_NOTE_COL_STATUS + " = ?";
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery(readNormalNotes, new String[]{NoteStatus.DELETED.toString()});
    }
    public Cursor getAllNotes() {
        String readNormalNotes = "SELECT * FROM " + TABLE_NOTE;
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery(readNormalNotes, null);
    }


    public void importFromJSON(NoteExportModel[] notes) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        for (NoteExportModel note : notes) {
            values.put(TABLE_NOTE_COL_TITLE, note.getTitle());
            values.put(TABLE_NOTE_COL_CONTENT, note.getContent());
            values.put(TABLE_NOTE_COL_DATE, note.getDateCreation());
            values.put(TABLE_NOTE_COL_STATUS, note.getStatus());
            values.put(TABLE_NOTE_COL_isFAVORITE, note.getIsFavorite());
            database.insert(TABLE_NOTE, null, values);
        }
        Toast.makeText(context, "Import Complete", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<NoteExportModel> exportNotesToJSON() {
        ArrayList<NoteExportModel> notesToExport = new ArrayList<>();
        Cursor cursor = getAllNotes();

        while (cursor.moveToNext()) {
            notesToExport.add(new NoteExportModel(
                    cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4),
                    cursor.getString(5)
            ));
        }
        return notesToExport;
    }
}