package com.sujeetarya.android.notes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.sujeetarya.android.notes.data.NoteContract.NoteEntry;

public class NoteDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "note.db";

    public NoteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the notes table
        String SQL_CREATE_PETS_TABLE = "CREATE TABLE " + NoteEntry.TABLE_NAME+"("
                + NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NoteEntry.COLUMN_NOTE_DATE + " TEXT NOT NULL, "
                + NoteEntry.COLUMN_NOTE_TITLE + " TEXT, "
                + NoteEntry.COLUMN_NOTE_TIME + " INTEGER NOT NULL, "
                + NoteEntry.COLUMN_NOTE_DESCRIPTION + " INTEGER NOT NULL DEFAULT 0);";
        // Execute the SQL statement
        db.execSQL(SQL_CREATE_PETS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
