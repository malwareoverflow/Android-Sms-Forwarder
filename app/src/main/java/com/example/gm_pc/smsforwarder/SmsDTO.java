package com.example.gm_pc.smsforwarder;

public class SmsDTO {
    public  Integer ID;
    public  String FROMNUMBER;
    public  String TONUMBER;
    public  String CREATED_ON;
    public  boolean ISACTIVE;
    public  String SMSLIMIT;



    public int getId() {
        return ID;
    }

    public void setId(int id) {
        this.ID = id;
    }

    public String getFROMNUMBER() {
        return FROMNUMBER;
    }

    public void setFROMNUMBER(String fromnumber) {
        this.FROMNUMBER=fromnumber;
    }

    public String getTONUMBER() {
        return TONUMBER;
    }

    public void setCREATED_ON(String CREATED_ON) {
        this.CREATED_ON=CREATED_ON;
    }

    public String getSMSLIMIT() {
        return SMSLIMIT;
    }
    public boolean getIACTIVE(){return ISACTIVE;}

    public void setISACTIVE(boolean isactive) {
        this.ISACTIVE=isactive;
    }

    public void setSMSLIMIT(String smslimit) {
        this.SMSLIMIT=smslimit;
    }


    public void setTONUMBER(String tonumber) {
        this.TONUMBER=tonumber;
    }
}
