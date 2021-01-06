package com.sujeetarya.android.notes;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.sujeetarya.android.notes.data.NoteContract;

public class NoteCursorAdapter extends CursorAdapter {
    public NoteCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView title = view.findViewById(R.id.tv_title);
        TextView description = view.findViewById(R.id.tv_description);
        TextView date = view.findViewById(R.id.tv_date);
        TextView time = view.findViewById(R.id.tv_time);

        int titleColumnIndex = cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE_TITLE);
        int descriptionColumnIndex = cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE_DESCRIPTION);
        int dateColumnIndex = cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE_DATE);
        int timeColumnIndex = cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE_TIME);

        time.setText(cursor.getString(timeColumnIndex));
        description.setText(cursor.getString(descriptionColumnIndex));
        date.setText(cursor.getString(dateColumnIndex));
        time.setText(cursor.getString(timeColumnIndex));

    }
}
