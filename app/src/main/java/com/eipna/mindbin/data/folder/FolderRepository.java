package com.eipna.mindbin.data.folder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.eipna.mindbin.data.Database;
import com.eipna.mindbin.data.note.Note;

import java.util.ArrayList;

public class FolderRepository extends Database {

    public FolderRepository(@Nullable Context context) {
        super(context);
    }

    public boolean create(Folder createdFolder) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOLDER_ID, createdFolder.getUUID());
        values.put(COLUMN_FOLDER_NAME, createdFolder.getName());
        values.put(COLUMN_FOLDER_DESCRIPTION, createdFolder.getDescription());
        values.put(COLUMN_FOLDER_PINNED, createdFolder.getIsPinned());

        long result = database.insert(TABLE_FOLDER, null, values);
        database.close();
        return result != -1;
    }

    public void edit(Folder editedFolder) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOLDER_NAME, editedFolder.getName());
        values.put(COLUMN_FOLDER_DESCRIPTION, editedFolder.getDescription());

        database.update(TABLE_FOLDER, values, COLUMN_FOLDER_ID + " = ?", new String[]{editedFolder.getUUID()});
        database.close();
    }

    public void editDescription(Folder editedFolder) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOLDER_DESCRIPTION, editedFolder.getDescription());
        database.update(TABLE_FOLDER, values, COLUMN_FOLDER_ID + " = ?", new String[]{editedFolder.getUUID()});
        database.close();
    }

    public void togglePin(String folderUUID, int isPinned) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOLDER_PINNED, isPinned);
        database.update(TABLE_FOLDER, values, COLUMN_FOLDER_ID + " = ?", new String[]{folderUUID});
        database.close();
    }

    public ArrayList<Folder> get() {
        ArrayList<Folder> list = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_FOLDER, null);

        if (cursor.moveToFirst()) {
            do {
                Folder folder = new Folder();
                folder.setUUID(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOLDER_ID)));
                folder.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOLDER_NAME)));
                folder.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOLDER_DESCRIPTION)));
                folder.setIsPinned(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FOLDER_PINNED)));
                list.add(folder);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return list;
    }

    public String[] getNames() {
        ArrayList<Folder> folders = get();
        String[] names = new String[folders.size() + 1];

        names[0] = "None";
        for (int i = 0; i < folders.size(); i++) {
            names[i + 1] = folders.get(i).getName();
        }
        return names;
    }

    public void delete(String folderUUID) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_FOLDER, COLUMN_FOLDER_ID + " = ?", new String[]{folderUUID});
        database.close();
    }

    public String getID(String name) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_FOLDER + " WHERE " + COLUMN_FOLDER_NAME + " = ?", new String[]{name});

        if (cursor.moveToFirst()) {
            String folderID = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOLDER_ID));
            cursor.close();
            database.close();
            return folderID;
        }
        cursor.close();
        database.close();
        return Note.NO_FOLDER;
    }

    public ArrayList<Note> getNotes(String folderID) {
        ArrayList<Note> list = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NOTE + " WHERE " + COLUMN_NOTE_FOLDER_ID + " = ?", new String[]{folderID});

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setUUID(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE_TITLE)));
                note.setContent(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE_CONTENT)));
                note.setState(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NOTE_STATE)));
                note.setFolderUUID(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE_FOLDER_ID)));
                note.setDateCreated(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_NOTE_DATE_CREATED)));
                list.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return list;
    }

    public String getName(String folderID) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_FOLDER + " WHERE " + COLUMN_FOLDER_ID + " = ?", new String[]{folderID});

        if (cursor.moveToFirst()) {
            String folderName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOLDER_NAME));
            cursor.close();
            database.close();
            return folderName;
        }
        cursor.close();
        database.close();
        return Note.NO_FOLDER;
    }
}