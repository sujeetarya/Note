
package com.sujeetarya.android.notes;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sujeetarya.android.notes.data.NoteContract.NoteEntry;
import com.sujeetarya.android.notes.data.NoteDbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;

    private NoteAdapter noteAdapter;
    static public List<Note> noteList = new ArrayList<>();
    public static NoteDbHelper mDbHelper;
    RecyclerView recyclerView;
    private ImageView noNoteImageView;
    static Note note;
    private TextView noNoteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        noNoteTextView = findViewById(R.id.empty_note);
        recyclerView = findViewById(R.id.recycle_view_note);
        noNoteImageView = findViewById(R.id.iv_no_note);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        noteAdapter = new NoteAdapter(this, noteList);
        recyclerView.setAdapter(noteAdapter);
        //displayView.setText(display);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        mDbHelper = new NoteDbHelper(getApplicationContext());
        setData();
        showEmptyNote();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {

                //creating and initializing an Intent object
                Intent intent = new Intent(getApplicationContext(), ViewNoteActivity.class);
                //attach the key value pair using putExtra to this intent
                intent.putExtra("R_VIEW_POSITION", position);
                //starting the activity
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

                showActionsDialog(position);
            }
        }));

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, AddNotesActivity.class);
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                noteAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.recreate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setData();
        showEmptyNote();
    }

    private void setData() {
        noteList.clear();
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + NoteEntry.TABLE_NAME, null);
        if (c.moveToFirst()) {

            do {

                note = new Note(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4));
                noteList.add(note);

            } while (c.moveToNext());
        }

    }

    // For testing purpose
    // call to insert the dummy note
    private void insertDummyNote() {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(NoteEntry.COLUMN_NOTE_DATE, "Feb 23,2020");
        values.put(NoteEntry.COLUMN_NOTE_TIME, "03:33 PM");
        values.put(NoteEntry.COLUMN_NOTE_TITLE, "title");
        values.put(NoteEntry.COLUMN_NOTE_DESCRIPTION, "Description");

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(NoteEntry.TABLE_NAME, null, values);
        Log.v("CatalogActivity", "New Row ID" + newRowId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            /*case R.id.action_insert_dummy_data:
                insertDummyNote();
                //displayDatabaseInfo();
                return true;*/
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Delete all the entries
                new AlertDialog.Builder(this)
                        .setTitle("Choose Option")
                        .setMessage("Delete all notes ?")
                        .setIcon(android.R.drawable.ic_delete)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                                db.delete(NoteEntry.TABLE_NAME, null, null);
                                db.execSQL("DELETE FROM " + NoteEntry.TABLE_NAME);
                                db.close();
                                CatalogActivity.this.recreate();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * To check wheather ay note is available to show or not
     * if not then show noNoteTextView
     */
    private void showEmptyNote() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("Select * from " + NoteEntry.TABLE_NAME, null);
        int count = c.getCount();
        c.close();

        if (count == 0) {
            noNoteTextView.setVisibility(View.VISIBLE);
            noNoteImageView.setVisibility(View.VISIBLE);
        } else {
            noNoteTextView.setVisibility(View.GONE);
            noNoteImageView.setVisibility(View.GONE);
        }
    }

    private void showActionsDialog(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(CatalogActivity.this);
        builder
                .setCancelable(false).setTitle("Choose Option").setMessage("Delete or Update Memo?")
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        //creating and initializing an Intent object
                        Intent intent = new Intent(getApplicationContext(), UpdateActivity.class);
                        //attach the key value pair using putExtra to this intent
                        intent.putExtra("R_VIEW_POSITION", position);
                        //starting the activity
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {

                                // delete the data from database
                                deleteData(position);

                                // remove from list
                                noteList.remove(position);

                                recyclerView.removeViewAt(position);
                                noteAdapter.notifyItemRemoved(position);
                                noteAdapter.notifyItemRangeChanged(position, noteList.size());
                                noteAdapter.notifyDataSetChanged();

                                showEmptyNote();
                            }
                        })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // delete the note from database at given position
    private void deleteData(int position) {

        note = noteList.get(position);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(NoteEntry.TABLE_NAME, "_id=" + note.getId(), null);
        db.close();

    }

}
