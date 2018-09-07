package com.example.gm_pc.smsforwarder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SettingsDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "SmsSettings";
    private static Context ctx;




    private static final String SQL_CREATE_SETTINGS_ENTRIES =
            "CREATE TABLE " + SettingsContract.SettingsEntry.TABLE_NAME + " (" +
                    SettingsContract.SettingsEntry._ID + "  INTEGER PRIMARY KEY AUTOINCREMENT ," +
                    SettingsContract.SettingsEntry.COLUMN_NAME_isEnableReceiver+ " TEXT ," +


                    SettingsContract.SettingsEntry.COLUMN_NAME_isDeleteSendSms + " TEXT )";


    private static final String SQL_INSERT_SETTINS_ENTRIES= "INSERT into "+SettingsContract.SettingsEntry.TABLE_NAME+" ( "+ SettingsContract.SettingsEntry.COLUMN_NAME_isEnableReceiver+ " , " + SettingsContract.SettingsEntry.COLUMN_NAME_isDeleteSendSms+ ")values(1,0)";

    private static final String SQL_SETTINGS_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SettingsContract.SettingsEntry.TABLE_NAME;


    private static final String SQL_DELETE_SETTINGS_ROW = "DELETE FROM " + SettingsContract.SettingsEntry.TABLE_NAME;

    public SettingsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctx=context;

    }
    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(SQL_CREATE_SETTINGS_ENTRIES);
        db.execSQL(SQL_INSERT_SETTINS_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_SETTINGS_ROW);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }




    public boolean Update(String Id, boolean isenablereceiver, boolean isdeletesendsms) {
        SQLiteDatabase db = this.getWritableDatabase();

// New value for one column
        try {
            ContentValues values = new ContentValues();
            values.put(SettingsContract.SettingsEntry.COLUMN_NAME_isEnableReceiver, isenablereceiver);
            values.put(SettingsContract.SettingsEntry.COLUMN_NAME_isDeleteSendSms, isdeletesendsms);




// Which row to update, based on the title
            String selection = SettingsContract.SettingsEntry._ID + " LIKE ?";
            String[] selectionArgs = {Id};

            int count = db.update(
                    SettingsContract.SettingsEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
            return true;
        } catch (Exception ex) {

            return false;
        }
    }


    public boolean getisDeletesmsstatus()
    {
        try{
            SQLiteDatabase db = this.getReadableDatabase();

            //  String s = "SELECT " + SmsContract.SmsEntry.COLUMN_NAME_FROMNUMBER + " From " + SmsContract.SmsEntry.TABLE_NAME + " WHERE " + SmsContract.SmsEntry.COLUMN_NAME_FROMNUMBER + " LIKE '%" + receivefrom + "%'";
            Cursor cursor = db.rawQuery("SELECT " +SettingsContract.SettingsEntry.COLUMN_NAME_isDeleteSendSms+
                            " From " + SettingsContract.SettingsEntry.TABLE_NAME +
                            " WHERE "+SettingsContract.SettingsEntry._ID+" = 1"
                    , null);
            if ((cursor.moveToFirst())) {
                do {

                    String val = cursor.getString(cursor.getColumnIndex(SettingsContract.SettingsEntry.COLUMN_NAME_isDeleteSendSms));
//                    Toast.makeText(ctx, "Inside db found "+val, Toast.LENGTH_LONG).show();

                    if (val != null && !val.isEmpty()) {
                        if(val.equals("1")){
                            return true;
                        }
                        return false;
                    }
                } while (cursor.moveToNext());

            }


        }
        catch (Exception ex){
            return false;
        }
        return false;
    }





    public boolean getisEnablereceiverstatus()
    {
        try{
            SQLiteDatabase db = this.getReadableDatabase();

            //  String s = "SELECT " + SmsContract.SmsEntry.COLUMN_NAME_FROMNUMBER + " From " + SmsContract.SmsEntry.TABLE_NAME + " WHERE " + SmsContract.SmsEntry.COLUMN_NAME_FROMNUMBER + " LIKE '%" + receivefrom + "%'";
            Cursor cursor = db.rawQuery("SELECT " + SettingsContract.SettingsEntry.COLUMN_NAME_isEnableReceiver +
                            " From " + SettingsContract.SettingsEntry.TABLE_NAME +
                            " WHERE "+SettingsContract.SettingsEntry._ID+" = 1"
                    , null);
            if ((cursor.moveToFirst())) {
                do {

                    String val = cursor.getString(cursor.getColumnIndex(SettingsContract.SettingsEntry.COLUMN_NAME_isEnableReceiver));
//                    Toast.makeText(ctx, "Inside db found "+val, Toast.LENGTH_LONG).show();

                    if (val != null && !val.isEmpty()) {
                        if(val.equals("1")){
                            return true;
                        }
                    }
                } while (cursor.moveToNext());

            }


        }
        catch (Exception ex){
            return false;
        }
        return false;
    }
}
