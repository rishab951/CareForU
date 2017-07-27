package com.cbitss.careforu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harpreet on 12/24/2016.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ReminderManager";

    // table name
    private static final String TABLE_REMINDERS = "Reminders";
    private static final String TABLE_TIPS = "Tips";

    // Table Columns names

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_REMINDERS_TABLE = "CREATE TABLE " + TABLE_REMINDERS + "( counter INTEGER PRIMARY KEY , medname TEXT , medinfo  TEXT,"+
                "interval  INTEGER ,TimeUnit TEXT,Freq INTEGER,weekdays TEXT,Times TEXT, StartTime TEXT,StartDate DATE,EndDate DATE)";
        db.execSQL(CREATE_REMINDERS_TABLE);
        String CREATE_TIPS_TABLE = "CREATE TABLE " + TABLE_TIPS + "( id INTEGER PRIMARY KEY, title  TEXT,"+
                "intro_text TEXT )";
        db.execSQL(CREATE_TIPS_TABLE);
        Log.d("DatabaseHandler","Database Created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIPS);
        // Create tables again
        onCreate(db);
    }

    int deleteReminder(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_REMINDERS,"medname" + "='" + name + "'",null);
    }

    void addReminder(ArrayList al) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("counter", (int) al.get(0));
        values.put("medname", ""+al.get(1));
        values.put("medinfo", ""+al.get(2));
        values.put("interval", (int) al.get(3));
        values.put("TimeUnit", ""+al.get(4));
        values.put("Freq", ""+al.get(5));
        values.put("weekdays", ""+al.get(6));
        values.put("Times",""+al.get(7));
        values.put("StartTime", ""+al.get(8));
        values.put("StartDate", ""+al.get(9));
        values.put("EndDate", ""+al.get(10));

        // Inserting Row
        db.insert(TABLE_REMINDERS, null, values);
        Log.d("Inserted:","Row Inserted");
        db.close(); // Closing database connection

        ArrayList all=new ArrayList();
        all.add(al.get(0));
        all.add(al.get(1));
        //final boolean hi = al.add("hi");
        FragmentReminder rm=new FragmentReminder();
        rm.receiveData(al);
    }

    void addTips(ArrayList al) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", (int)al.get(0));
        values.put("title", ""+al.get(1));
        values.put("intro_text", ""+al.get(2));

        // Inserting Row
        db.insert(TABLE_TIPS, null, values);
        Log.d("Inserted:",values+"");
        db.close(); // Closing database connection
    }



    // Getting single contact
    ArrayList getReminder(int count) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_REMINDERS, new String[] { "counter","medname",
                        "medinfo", "interval","TimeUnit","Freq","weekdays","Times","StartTime","StartDate","EndDate"}, "counter" + "=?",
                new String[] { ""+count },null,null,null,null);
        if (cursor != null)
            cursor.moveToFirst();

        ArrayList al=new ArrayList();

        al.add(cursor.getString(0));al.add(cursor.getString(1));al.add(cursor.getString(2));al.add(cursor.getString(3));
        al.add(cursor.getString(4));al.add(cursor.getString(5));al.add(cursor.getString(6));al.add(cursor.getString(7));
        al.add(cursor.getString(8));al.add(cursor.getString(9));al.add(cursor.getString(10));

        return al;

    }

    // Getting All Contacts
    public ArrayList getAllReminders() {
        ArrayList ReminderList = new ArrayList();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REMINDERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()){
            do{
                ReminderList.add(cursor.getString(1));
            }while(cursor.moveToNext());
            db.close();
        }

        return ReminderList;
    }

    public List getAllTips(String path) {
        List<FirstAidCard> TipsList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TIPS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                TipsList.add(new FirstAidCard(Integer.parseInt(cursor.getString(0)),cursor.getString(1),
                        cursor.getString(2),"file://"+path+"/"+cursor.getString(0)+"_0.jpg"));
            } while (cursor.moveToNext());
        }
        db.close();

        return TipsList;
    }


    // Getting Reminder Count
    public int getRemindersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_REMINDERS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if(cursor.getCount()==0)
            return 0;
        // looping through all rows and adding to list
        cursor.moveToLast();
        int count=Integer.parseInt(cursor.getString(0));

        db.close();
        return count;
    }

    public ArrayList getReminder(String s) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_REMINDERS, new String[] { "counter","medname",
                        "medinfo", "interval","TimeUnit","Freq","weekdays","Times","StartTime","StartDate","EndDate"}, "medname" + "=?",
                new String[] {s },null,null,null,null);
        if (cursor != null)
            cursor.moveToFirst();

        ArrayList al=new ArrayList();

        al.add(cursor.getString(0));al.add(cursor.getString(1));al.add(cursor.getString(2));al.add(cursor.getString(3));
        al.add(cursor.getString(4));al.add(cursor.getString(5));al.add(cursor.getString(6));al.add(cursor.getString(7));
        al.add(cursor.getString(8));al.add(cursor.getString(9));al.add(cursor.getString(10));

        return al;
    }
}

