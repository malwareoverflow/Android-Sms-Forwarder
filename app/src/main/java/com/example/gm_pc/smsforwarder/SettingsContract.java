package com.example.gm_pc.smsforwarder;

import android.provider.BaseColumns;

public class SettingsContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private SettingsContract() {}

    /* Inner class that defines the table contents */
    public static class SettingsEntry implements BaseColumns {
        public static final String TABLE_NAME = "Settings";

        public static final String COLUMN_NAME_isEnableReceiver = "isEnableReceiver";
        public static final String COLUMN_NAME_isDeleteSendSms = "isDeleteSendSms";

    }


}
