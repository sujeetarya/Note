package com.sujeetarya.android.notes;

public class Note {

    private int mId;
    private String mDate;
    private String mTime;
    private String mTitle;
    private String mDescription;

    public Note(int id, String date, String time, String description, String title) {
        mId = id;
        mDate = date;
        mTime = time;
        mTitle = title;
        mDescription = description;
    }

    //  Getter Methods
    public int getId() {
        return mId;
    }

    public String getDate() {
        return mDate;
    }

    public String getTime() {
        return mTime;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    // Setter Methods


    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public void setTime(String mTime) {
        this.mTime = mTime;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }
}
