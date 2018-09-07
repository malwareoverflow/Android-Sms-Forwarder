package com.example.gm_pc.smsforwarder;

import android.provider.BaseColumns;

public final class SmsContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private SmsContract() {}

    /* Inner class that defines the table contents */
    public static class SmsEntry implements BaseColumns {
        public static final String TABLE_NAME = "Sms";

        public static final String COLUMN_NAME_FROMNUMBER = "FROMNUMBER";
        public static final String COLUMN_NAME_TONUMBER = "TONUMBER";
        public static final String COLUMN_NAME_CREATED_ON = "CREATED_ON";
        public static final String COLUMN_NAME_ISACTIVE = "ISACTIVE";
        public static final String COLUMN_NAME_SMSLIMIT = "SMSLIMIT";
    }





}
