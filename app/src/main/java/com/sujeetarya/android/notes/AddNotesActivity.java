package com.sujeetarya.android.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sujeetarya.android.notes.data.NoteContract;
import com.sujeetarya.android.notes.data.NoteDbHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddNotesActivity extends AppCompatActivity implements
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


    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    FloatingActionButton floatingActionButtonSave;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        // Find all relevant views that we will need to read user input from
        mDateEditText = findViewById(R.id.edt_date);
        mTimeEditText = findViewById(R.id.edt_time);
        mTitleEditText = findViewById(R.id.edt_title);
        mDescriptionEditText = findViewById(R.id.edt_description);


        btnDatePicker = findViewById(R.id.btn_date);
        btnTimePicker = findViewById(R.id.btn_time);
        txtDate = findViewById(R.id.edt_date);
        txtTime = findViewById(R.id.edt_time);
        floatingActionButtonSave = findViewById(R.id.fab_update);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        floatingActionButtonSave.setOnClickListener(this);

        setLoadDateTime();

    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

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

        if (v == floatingActionButtonSave) {

            if (!isEmpty()) {
                // Save note to database
                insertNote();
                //  finish the current activity
                finish();
            }
        }
    }

    // Set date and time for the 1st time when activity is created
    private void setLoadDateTime() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        txtDate.setText(formatDate(mDay, mMonth, mYear));

        // Get Current Time
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        txtTime.setText(formatTime(mHour, mMinute));
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
        getMenuInflater().inflate(R.menu.menu_add_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // check weather Edit text is empty
                if (!isEmpty()) {
                    // Save note to database
                    insertNote();
                    //  finish the current activity and return
                    finish();
                }
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            /*case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;*/
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     *   Get user input from editor and save new note into database
     * */
    private void insertNote() {
        // Gets the data repository in write mode
        NoteDbHelper mDbHelper = new NoteDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //  trim method remove the extra white spaces
        String dateString = mDateEditText.getText().toString().trim();
        String timeString = mTimeEditText.getText().toString().trim();
        String title = mTitleEditText.getText().toString().trim();
        String description = mDescriptionEditText.getText().toString().trim();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(NoteContract.NoteEntry.COLUMN_NOTE_DATE, dateString);
        values.put(NoteContract.NoteEntry.COLUMN_NOTE_TITLE, timeString);
        values.put(NoteContract.NoteEntry.COLUMN_NOTE_DESCRIPTION, title);
        values.put(NoteContract.NoteEntry.COLUMN_NOTE_TIME, description);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(NoteContract.NoteEntry.TABLE_NAME, null, values);
        //Toast.makeText(this, "ID=" + newRowId, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Note Added", Toast.LENGTH_SHORT).show();
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
