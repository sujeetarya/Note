package com.sujeetarya.android.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sujeetarya.android.notes.data.NoteContract;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UpdateActivity extends AppCompatActivity implements
        View.OnClickListener {

    /**
     * EditText field to enter the note's date
     */
    private EditText mDateEditText;

    /**
     * EditText field to enter the notes's time
     */
    private EditText mTimeEditText;

    /**
     * EditText field to enter the notes's title
     */
    private EditText mTitleEditText;

    /**
     * EditText field to enter the notes's description
     */
    private EditText mDescriptionEditText;

    int position;

    ImageButton btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    FloatingActionButton floatingActionButtonUpdate;

    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // Find all relevant views that we will need
        mDateEditText = findViewById(R.id.edt_date);
        mTimeEditText = findViewById(R.id.edt_time);
        mTitleEditText = findViewById(R.id.edt_title);
        mDescriptionEditText = findViewById(R.id.edt_description);


        btnDatePicker = findViewById(R.id.btn_date);
        btnTimePicker = findViewById(R.id.btn_time);
        txtDate = findViewById(R.id.edt_date);
        txtTime = findViewById(R.id.edt_time);
        floatingActionButtonUpdate = findViewById(R.id.fab_update);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        floatingActionButtonUpdate.setOnClickListener(this);

        //get the current intent
        Intent intent = getIntent();

        //get the attached extras from the intent
        // RecycleView position on which clicked
        position = intent.getIntExtra("R_VIEW_POSITION", 0);

        setData(position);

    }

    @Override
    public void onClick(View v) {


        // date picker button click handled
        if (v == btnDatePicker) {

            txtDate.getText().toString();

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DateFormat dateFormat = DateFormat.getDateInstance();
            //dateFormat.format(c.getTime());


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            //txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            txtDate.setText(formatDate(dayOfMonth, monthOfYear + 1, year));

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        // time picker button click handled
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(formatTime(hourOfDay, minute));
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }

        // floating action button update click handled
        if (v == floatingActionButtonUpdate) {

            if (!isEmpty()) {

                //Toast.makeText(UpdateActivity.this, "Update method call !", Toast.LENGTH_SHORT).show();
                updateNote(mDateEditText.getText().toString(),
                        mTimeEditText.getText().toString(),
                        mTitleEditText.getText().toString(),
                        mDescriptionEditText.getText().toString(), position);

                //  finish the current activity
                finish();

            }
            //  finish the current activity
            //finish();
        }
    }


    // To format the time in 12h
    private String formatTime(int hour, int minute) {
        String time = hour + "." + minute;
        String formattedTime = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH.mm");
        try {
            Date timeOfTypeDate = simpleDateFormat.parse(time);
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm aa");
            formattedTime = simpleDateFormat2.format(timeOfTypeDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedTime;
    }

    // To format the date
    private String formatDate(int day, int month, int year) {
        String date = day + "/" + month + "/" + year;
        String formattedDate = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dateOfTypeDate = simpleDateFormat.parse(date);
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MMM dd, yyyy");
            formattedDate = simpleDateFormat2.format(dateOfTypeDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_update:
                if (!isEmpty()) {
                    //Toast.makeText(UpdateActivity.this, "Update method call !", Toast.LENGTH_SHORT).show();
                    updateNote(mDateEditText.getText().toString(),
                            mTimeEditText.getText().toString(),
                            mTitleEditText.getText().toString(),
                            mDescriptionEditText.getText().toString(), position);

                    //  finish the current activity
                    finish();
                }
                return true;
            /*// Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;*/
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @param position of the view in recycle view
     * @updateData set the data from recycleview to @activity_update
     */
    private void setData(final int position) {

        CatalogActivity.note = CatalogActivity.noteList.get(position);
        if (CatalogActivity.note != null) {

            mDateEditText.setText(CatalogActivity.note.getDate());
            mTimeEditText.setText(CatalogActivity.note.getTime());
            mTitleEditText.setText(CatalogActivity.note.getTitle());
            mDescriptionEditText.setText(CatalogActivity.note.getDescription());
        }

    }

    private void updateNote(String date, String time, String title, String description, final int position) {

        CatalogActivity.note = CatalogActivity.noteList.get(position);
        // Set the data to update
        CatalogActivity.note.setDate(date);
        CatalogActivity.note.setTime(time);
        CatalogActivity.note.setTitle(title);
        CatalogActivity.note.setDescription(description);

        SQLiteDatabase db = CatalogActivity.mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NoteContract.NoteEntry.COLUMN_NOTE_DATE, CatalogActivity.note.getDate());
        values.put(NoteContract.NoteEntry.COLUMN_NOTE_TITLE, CatalogActivity.note.getTime());
        values.put(NoteContract.NoteEntry.COLUMN_NOTE_TIME, CatalogActivity.note.getDescription());
        values.put(NoteContract.NoteEntry.COLUMN_NOTE_DESCRIPTION, CatalogActivity.note.getTitle());

        db.update(NoteContract.NoteEntry.TABLE_NAME, values, "_id=" + CatalogActivity.note.getId(), null);

    }

    /**
     * @return whether the @mTitleEditText and @mDescriptionEditText is empty or not
     */
    private boolean isEmpty() {
        if (TextUtils.isEmpty(mTitleEditText.getText().toString())) {
            mTitleEditText.requestFocus();
            mTitleEditText.setError("Enter Title!");
            //Toast.makeText(UpdateActivity.this, "Enter Title!", Toast.LENGTH_SHORT).show();
            return true;

        } else if (TextUtils.isEmpty(mDescriptionEditText.getText().toString())) {
            mDescriptionEditText.requestFocus();
            mDescriptionEditText.setError("Enter Description!");
            //Toast.makeText(UpdateActivity.this, "Enter Description!", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }
}
