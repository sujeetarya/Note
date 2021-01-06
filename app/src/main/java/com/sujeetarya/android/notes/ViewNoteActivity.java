package com.sujeetarya.android.notes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.sujeetarya.android.notes.data.NoteContract;

public class ViewNoteActivity extends AppCompatActivity {

    private TextView mTextViewDate;
    private TextView mTextViewTime;
    private TextView mTextViewTitle;
    private TextView mTextViewDescription;

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        //get the current intent
        Intent intent = getIntent();

        //get the attached extras from the intent
        // RecycleView position on which clicked
        position = intent.getIntExtra("R_VIEW_POSITION", 0);


        // Find all relevant views that we will need
        mTextViewDate = findViewById(R.id.text_view_date);
        mTextViewTime = findViewById(R.id.text_view_time);
        mTextViewTitle = findViewById(R.id.text_view_title);
        mTextViewDescription = findViewById(R.id.text_view_description);

        setData(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_view_note.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_view_note, menu);
        return true;
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        this.recreate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setData(position);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the refresh icon to recreate the activity
            case R.id.action_edit:
                //creating and initializing an Intent object
                Intent intent = new Intent(getApplicationContext(), UpdateActivity.class);
                //attach the key value pair using putExtra to this intent
                intent.putExtra("R_VIEW_POSITION", position);
                //starting the activity
                startActivity(intent);
                finish();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete:
                // Delete all the entries
                new AlertDialog.Builder(this)
                        .setTitle("Choose Option")
                        .setMessage("Delete this note ?")
                        .setIcon(android.R.drawable.ic_delete)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteData(position);
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @param position of the view in recycle view
     * @updateData set the data from recycleview to @activity_view_note
     */
    private void setData(final int position) {

        CatalogActivity.note = CatalogActivity.noteList.get(position);
        if (CatalogActivity.note != null) {

            mTextViewDate.setText(CatalogActivity.note.getDate());
            mTextViewTime.setText(CatalogActivity.note.getTime());
            mTextViewTitle.setText(CatalogActivity.note.getTitle());
            mTextViewDescription.setText(CatalogActivity.note.getDescription());
        }

    }

    // delete the note from database at given position
    private void deleteData(int position) {

        CatalogActivity.note = CatalogActivity.noteList.get(position);
        SQLiteDatabase db = CatalogActivity.mDbHelper.getWritableDatabase();
        db.delete(NoteContract.NoteEntry.TABLE_NAME, "_id=" + CatalogActivity.note.getId(), null);
        db.close();

    }
}