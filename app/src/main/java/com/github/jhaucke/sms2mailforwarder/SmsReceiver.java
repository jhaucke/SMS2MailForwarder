package com.github.jhaucke.sms2mailforwarder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Jere on 10.05.16.
 */
public class SmsReceiver extends BroadcastReceiver {
    private String TAG = SmsReceiver.class.getSimpleName();

    public SmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle myBundle = intent.getExtras();
        SmsMessage [] messages = null;
        String originatingAddress = "";
        String strMessage = "";

        if (myBundle != null)
        {
            Object [] pdus = (Object[]) myBundle.get("pdus");

            messages = new SmsMessage[pdus.length];

            for (int i = 0; i < messages.length; i++)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    String format = myBundle.getString("format");
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                }
                else {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                if (originatingAddress.equals("")) {
                    originatingAddress = context.getString(R.string.from) + messages[i].getOriginatingAddress();
                }
                strMessage += messages[i].getMessageBody();
            }

            Log.d("SMS", strMessage);

            Intent serviceIntent = new Intent(context, SendMailIntentService.class);
            serviceIntent.putExtra(Constants.EXTRA_SUBJECT, context.getString(R.string.mail_subject));
            serviceIntent.putExtra(Constants.EXTRA_BODY, originatingAddress + "\n\n" + strMessage);
            context.startService(serviceIntent);
        }
    }
}
