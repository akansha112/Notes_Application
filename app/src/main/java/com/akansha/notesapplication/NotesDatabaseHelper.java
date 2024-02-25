package com.akansha.notesapplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class NotesDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes_database";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "notes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_TITLE
            + " text not null, " + COLUMN_CONTENT
            + " text not null);";

    public NotesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
// Insert a new note into the database
public void insertNote(NotesModel note) {
    SQLiteDatabase db = this.getWritableDatabase();
    long rowId = -1;

    try {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_CONTENT, note.getContent());

        // Insert the new row, returning the primary key value of the new row
        rowId = db.insert(TABLE_NAME, null, values);
    } catch (SQLException e) {
        // Handle any potential exceptions
        e.printStackTrace();
    } finally {
        // Close the database connection
        db.close();
    }

}

    public List<NotesModel> getAllNotes() {
        List<NotesModel> notesList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT));
            NotesModel note = new NotesModel(id, title, content);
            notesList.add(note);
        }

        cursor.close();
        db.close();
        return notesList;
    }
    public void updateNote(NotesModel note) {
        SQLiteDatabase db = getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, note.getTitle());
            values.put(COLUMN_CONTENT, note.getContent());

            String whereClause = COLUMN_ID + "=?";
            String[] whereArgs = {String.valueOf(note.getId())};

            // Update the existing row in the database
            db.update(TABLE_NAME, values, whereClause, whereArgs);
        } catch (SQLException e) {
            // Handle any potential exceptions
            e.printStackTrace();
        } finally {
            // Close the database connection
            db.close();
        }
    }
    public NotesModel getNoteById(int noteId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + noteId;
        Cursor cursor = db.rawQuery(query, null);

        int id = -1;
        String title = "";
        String content = "";

        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
            content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT));
        }

        cursor.close();
        db.close();

        return new NotesModel(id, title, content);
    }
    public void deleteNote(int noteId) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = COLUMN_ID + "=?";
        String[] whereArgs = {String.valueOf(noteId)};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

}

