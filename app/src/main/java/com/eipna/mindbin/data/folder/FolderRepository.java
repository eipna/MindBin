package com.eipna.mindbin.data.folder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.eipna.mindbin.data.Database;

import java.util.ArrayList;

public class FolderRepository extends Database {

    public FolderRepository(@Nullable Context context) {
        super(context);
    }

    public boolean create(Folder createdFolder) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOLDER_ID, createdFolder.getIsPinned());
        values.put(COLUMN_FOLDER_NAME, createdFolder.getName());
        values.put(COLUMN_FOLDER_DESCRIPTION, createdFolder.getDescription());
        values.put(COLUMN_FOLDER_PINNED, createdFolder.getIsPinned());

        long result = database.insert(TABLE_FOLDER, null, values);
        database.close();
        return result != -1;
    }

    public boolean edit(Folder editedFolder) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOLDER_NAME, editedFolder.getName());
        values.put(COLUMN_FOLDER_DESCRIPTION, editedFolder.getDescription());

        long result = database.update(TABLE_FOLDER, values, COLUMN_FOLDER_ID + " = ?", new String[]{editedFolder.getUUID()});
        database.close();
        return result != -1;
    }

    public void togglePin(Folder folder) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOLDER_PINNED, folder.getIsPinned());
        database.update(TABLE_FOLDER, values, COLUMN_FOLDER_ID + " = ?", new String[]{folder.getUUID()});
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

    public void delete(String folderUUID) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_FOLDER, COLUMN_FOLDER_ID + " = ?", new String[]{folderUUID});
        database.close();
    }
}