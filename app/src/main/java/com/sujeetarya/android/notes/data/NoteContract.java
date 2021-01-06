package com.sujeetarya.android.notes.data;

import android.provider.BaseColumns;

public final class NoteContract {

    private NoteContract(){}

    public static class NoteEntry implements BaseColumns{
        //  table name
        public static final String TABLE_NAME = "NotesEntry";
        //  column names
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NOTE_DATE = "date";
        public static final String COLUMN_NOTE_TIME = "time";
        public static final String COLUMN_NOTE_TITLE = "title";
        public static final String COLUMN_NOTE_DESCRIPTION = "description";

    }
}
