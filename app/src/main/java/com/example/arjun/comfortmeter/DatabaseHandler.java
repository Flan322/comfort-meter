package com.example.arjun.comfortmeter;

import android.content.ContentValues;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jjoe64.graphview.series.DataPoint;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "comfortMeter.db";

    //Table name
    private static final String Table_Sessions = "Sessions";
    private static final String Table_Data = "Data";

    //Table Columns names
    private static final String Session_id = "id";
    private static final String Session_date = "date";
    private static final String Session_starttime = "starttime";
    private static final String data_id = "dataid";
    private static final String data_time = "curtime"; //This would be the time elapsed after the start time technically
    private static final String data_jerk = "jerk";
    private Integer last_session;


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    public void onCreate(SQLiteDatabase db) {
        String Create_Session_table = "CREATE TABLE " + Table_Sessions + " ("
                + Session_id + " INTEGER PRIMARY KEY AUTOINCREMENT," + Session_date + " TEXT,"
                + Session_starttime + " TEXT)";
        db.execSQL(Create_Session_table);

        String Create_data_table = "CREATE TABLE " + Table_Data + " ("
                + data_id + " INTEGER," + data_time + " DOUBLE, FOREIGN KEY ("
                + data_id + ") REFERENCES " + Table_Sessions + " ("
                + Session_id + ") ON DELETE CASCADE";
        db.execSQL(Create_data_table);
    }

    public void addSession(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.setForeignKeyConstraintsEnabled(true);
        ContentValues values = new ContentValues();

        Calendar date = Calendar.getInstance();

        SimpleDateFormat year = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");

        String strDate = year.format(date.getTime());
        String strTime = time.format(date.getTime());

        values.put(Session_date, strDate);
        values.put(Session_starttime, strTime);

        last_session = (int) db.insert(Table_Sessions, null, values);
        db.close();
    }

    public void addData(DataPoint data){
        SQLiteDatabase db = this.getWritableDatabase();
        db.setForeignKeyConstraintsEnabled(true);
        ContentValues values = new ContentValues();

        Double Time = data.getX();
        Double Magnitude = data.getY();

        values.put(data_id, last_session);
        values.put(data_time, Time);
        values.put(data_jerk, Magnitude);

        db.insert(Table_Data, null, values);
        db.close();
    }

    public void removeSessionData(int session){
        SQLiteDatabase db = this.getWritableDatabase();
        db.setForeignKeyConstraintsEnabled(true);

        db.delete(Table_Sessions,"id=?",new String[]{Integer.toString(session)});

    }



    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Table_Sessions);
        db.execSQL("DROP TABLE IF EXISTS " + Table_Data);

        // Create tables again
        onCreate(db);
    }

}
