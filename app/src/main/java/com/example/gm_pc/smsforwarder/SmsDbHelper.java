package com.example.gm_pc.smsforwarder;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SmsDbHelper   extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "LatestSms";
    private static Context ctx;
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + SmsContract.SmsEntry.TABLE_NAME + " (" +
                    SmsContract.SmsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                    SmsContract.SmsEntry.COLUMN_NAME_FROMNUMBER + " TEXT," +
                    SmsContract.SmsEntry.COLUMN_NAME_TONUMBER + " TEXT," +
                    SmsContract.SmsEntry.COLUMN_NAME_CREATED_ON + " TEXT," +
                    SmsContract.SmsEntry.COLUMN_NAME_ISACTIVE + " TEXT," +
                    SmsContract.SmsEntry.COLUMN_NAME_SMSLIMIT + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SmsContract.SmsEntry.TABLE_NAME;


    private static final String SQL_DELETE_ROW = "DELETE FROM " + SmsContract.SmsEntry.TABLE_NAME;

    public SmsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctx=context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean Insert(String FROMNUMBER, String TONUMBER, String CREATED_ON, boolean ISACTIVE, String SMS_LIMIT)
    {
        // Gets the data repository in write mode


// Create a new map of values, where column names are the keys
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SmsContract.SmsEntry.COLUMN_NAME_FROMNUMBER, FROMNUMBER);
        values.put(SmsContract.SmsEntry.COLUMN_NAME_TONUMBER, TONUMBER);
        values.put(SmsContract.SmsEntry.COLUMN_NAME_CREATED_ON, CREATED_ON);
        values.put(SmsContract.SmsEntry.COLUMN_NAME_ISACTIVE, ISACTIVE);
        values.put(SmsContract.SmsEntry.COLUMN_NAME_SMSLIMIT, SMS_LIMIT);

// Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(SmsContract.SmsEntry.TABLE_NAME, null, values);
        if (newRowId == -1)
            return false;
        else
            return true;
    }


    public boolean Update(String Id, String FROMNUMBER, String TONUMBER, String CREATED_ON, boolean ISACTIVE, String SMS_LIMIT) {
        SQLiteDatabase db = this.getWritableDatabase();

// New value for one column
        try {
            ContentValues values = new ContentValues();
            values.put(SmsContract.SmsEntry.COLUMN_NAME_FROMNUMBER, FROMNUMBER);
            values.put(SmsContract.SmsEntry.COLUMN_NAME_TONUMBER, TONUMBER);
            values.put(SmsContract.SmsEntry.COLUMN_NAME_CREATED_ON, CREATED_ON);
            values.put(SmsContract.SmsEntry.COLUMN_NAME_ISACTIVE, ISACTIVE);
            values.put(SmsContract.SmsEntry.COLUMN_NAME_SMSLIMIT, SMS_LIMIT);


// Which row to update, based on the title
            String selection = SmsContract.SmsEntry._ID + " LIKE ?";
            String[] selectionArgs = {Id};

            int count = db.update(
                    SmsContract.SmsEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
            return true;
        } catch (Exception ex) {

            return false;
        }
    }


    // Return sqlite database cursor object.
    public Cursor getAllAccountCursor() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + SmsContract.SmsEntry.TABLE_NAME, null);
        return cursor;
    }

    public boolean isSmslimitgreaterzero(String To,String From)
    {
        try{
            SQLiteDatabase db = this.getReadableDatabase();

            //  String s = "SELECT " + SmsContract.SmsEntry.COLUMN_NAME_FROMNUMBER + " From " + SmsContract.SmsEntry.TABLE_NAME + " WHERE " + SmsContract.SmsEntry.COLUMN_NAME_FROMNUMBER + " LIKE '%" + receivefrom + "%'";
            Cursor cursor = db.rawQuery("SELECT " + SmsContract.SmsEntry.COLUMN_NAME_SMSLIMIT +
                    " From " + SmsContract.SmsEntry.TABLE_NAME +
                    " WHERE "+SmsContract.SmsEntry.COLUMN_NAME_TONUMBER+
                    " LIKE '%" +To+ "%'"+
                    " AND " +SmsContract.SmsEntry.COLUMN_NAME_FROMNUMBER +
                    " LIKE '%" + From + "%'", null);
            if ((cursor.moveToFirst())) {
                do {

                    String val = cursor.getString(cursor.getColumnIndex(SmsContract.SmsEntry.COLUMN_NAME_SMSLIMIT));
//                    Toast.makeText(ctx, "Inside db found "+val, Toast.LENGTH_LONG).show();

                    if (val != null && !val.isEmpty()) {
                        if(Integer.valueOf(val)>0){
                            return  true;
                        }
                    }
                } while (cursor.moveToNext());

            }


        }
        catch (Exception ex){

        }
        return false;
    }
    public boolean decrementsmscount(String To ,String From)
    {

        try     {

            String updateSmsLimit = "UPDATE "+SmsContract.SmsEntry.TABLE_NAME+
                    " SET "+SmsContract.SmsEntry.COLUMN_NAME_SMSLIMIT +
                    " = "+ SmsContract.SmsEntry.COLUMN_NAME_SMSLIMIT +
                    " - 1 WHERE "+ SmsContract.SmsEntry.COLUMN_NAME_TONUMBER+
                    " LIKE '%" +To+ "%'"+
                    " AND "+SmsContract.SmsEntry.COLUMN_NAME_FROMNUMBER+
                    " LIKE '%" + From + "%'";
            SQLiteDatabase db = this.getReadableDatabase();

            db.execSQL(updateSmsLimit);

        }
        catch (Exception ex){


        }
        return  false;
    }


    public boolean checkifsameexist(String To,String from)
    {

        List<String> forwardtonumber = new ArrayList<String>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            //  String s = "SELECT " + SmsContract.SmsEntry.COLUMN_NAME_FROMNUMBER + " From " + SmsContract.SmsEntry.TABLE_NAME + " WHERE " + SmsContract.SmsEntry.COLUMN_NAME_FROMNUMBER + " LIKE '%" + receivefrom + "%'";
            Cursor cursor = db.rawQuery("SELECT " + SmsContract.SmsEntry.COLUMN_NAME_TONUMBER +
                    " From " + SmsContract.SmsEntry.TABLE_NAME +
                    " WHERE "+SmsContract.SmsEntry.COLUMN_NAME_TONUMBER+
                    " LIKE '%" +To+ "%'"+
                    " AND " +SmsContract.SmsEntry.COLUMN_NAME_FROMNUMBER +
                    " LIKE '%" + from + "%'", null);
            if ((cursor.moveToFirst())) {
                do {

                    String val = cursor.getString(cursor.getColumnIndex(SmsContract.SmsEntry.COLUMN_NAME_TONUMBER));
//                    Toast.makeText(ctx, "Inside db found "+val, Toast.LENGTH_LONG).show();

                    if (val != null && !val.isEmpty()) {
                        return true;
                    }
                } while (cursor.moveToNext());

            }

        } catch (Exception e) {


        }

        return false;

    }
    public void deleteAccount(int id) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            db.execSQL(SQL_DELETE_ROW + " WHERE " + SmsContract.SmsEntry._ID + " = " + id);
        } catch (Exception e) {


        }


    }

    public List<String>  getforwardifrecexist(String receivefrom) {
        List<String> forwardtonumber = new ArrayList<String>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            //  String s = "SELECT " + SmsContract.SmsEntry.COLUMN_NAME_FROMNUMBER + " From " + SmsContract.SmsEntry.TABLE_NAME + " WHERE " + SmsContract.SmsEntry.COLUMN_NAME_FROMNUMBER + " LIKE '%" + receivefrom + "%'";
            Cursor cursor = db.rawQuery("SELECT " + SmsContract.SmsEntry.COLUMN_NAME_TONUMBER +
                    " From " + SmsContract.SmsEntry.TABLE_NAME +
                    " WHERE "+SmsContract.SmsEntry.COLUMN_NAME_ISACTIVE+" = 1 "+
                    " AND " +SmsContract.SmsEntry.COLUMN_NAME_FROMNUMBER +
                    " LIKE '%" + receivefrom + "%'", null);
            if ((cursor.moveToFirst())) {
                do {

                    String val = cursor.getString(cursor.getColumnIndex(SmsContract.SmsEntry.COLUMN_NAME_TONUMBER));
//                    Toast.makeText(ctx, "Inside db found "+val, Toast.LENGTH_LONG).show();

                    if (val != null && !val.isEmpty()) {
                        forwardtonumber.add(val);
                    }
                } while (cursor.moveToNext());

            }
            return forwardtonumber;
        } catch (Exception e) {


        }
        return forwardtonumber;

    }


}


