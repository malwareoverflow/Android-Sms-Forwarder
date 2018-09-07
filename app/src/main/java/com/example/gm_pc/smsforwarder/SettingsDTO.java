package com.example.gm_pc.smsforwarder;

public class SettingsDTO {
    public  Integer ID;
    public  String isEnableReceiver;
    public  String isDeleteSendSms;




    public int getId() {
        return ID;
    }

    public void setId(int id) {
        this.ID = id;
    }

    public String getisEnableReceiver() {
        return isEnableReceiver;
    }

    public void setisEnableReceiver(String isenablereceiver) {
        this.isEnableReceiver=isenablereceiver;
    }

    public String getisDeleteSendSms() {
        return isDeleteSendSms;
    }


    public void setisDeleteSendSms(String isdeletesms) {
        this.isDeleteSendSms=isdeletesms;
    }



}
