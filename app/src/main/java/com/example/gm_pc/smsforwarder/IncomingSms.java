package com.example.gm_pc.smsforwarder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    SmsDbHelper mDbHelper ;
    SettingsDbHelper settingsDbHelper ;



    public void onReceive(Context context, Intent intent) {
        mDbHelper = new SmsDbHelper(context);

   settingsDbHelper = new SettingsDbHelper(context);
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();


        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                if( settingsDbHelper.getisEnablereceiverstatus()==false){
                    return;
                };
                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                  // senderNum =senderNum.replace("+92","0");
                  //  senderNum =phoeNumberWithOutCountryCode(senderNum);
                    senderNum =senderNum.trim();
                 //   message=message +" Sent by "+senderNum;
                    // Show Alert

                   int duration = Toast.LENGTH_LONG;
                    Toast   toastmsg = Toast.makeText(context,
                           "sender is "+senderNum, duration);

                    toastmsg.show();

                    List<String> forwardnumber=mDbHelper.getforwardifrecexist(senderNum);

                    if(!forwardnumber.isEmpty() ){

                        for (int j=0; j<forwardnumber.size(); j++) {

                            //if has sms sent limit
                            if(mDbHelper.isSmslimitgreaterzero(forwardnumber.get(j),senderNum)){
                                Toast   toast = Toast.makeText(context,
                                        !forwardnumber.isEmpty()?"Matched,sending sms now...":"Not Matched", duration);
                                toast.show();


                                if(sendSMS(forwardnumber.get(j),message))
                                {
                                    //decrement 1 from limit
                                    mDbHelper.decrementsmscount(forwardnumber.get(j),senderNum);
                                }else{

                                }
                            }
                            else{

                                Toast   toast = Toast.makeText(context,
                                        "Sorry you have reached your limit", duration);
                                toast.show();

                            }

                        }


                    }


                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }




    }

    private boolean sendSMS(String phoneNumber, String message) {
        try{
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, null, null);
            return true;
        }
        catch (Exception ex){

            return false;
        }
    }
    public String phoeNumberWithOutCountryCode(String phoneNumberWithCountryCode) {
        Pattern complie = Pattern.compile(" ");
        String[] phonenUmber = complie.split(phoneNumberWithCountryCode);
        Log.e("number is", phonenUmber[1]);
        return phonenUmber[1];
    }

}