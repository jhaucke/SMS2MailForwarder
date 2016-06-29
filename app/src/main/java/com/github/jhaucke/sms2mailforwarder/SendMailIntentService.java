package com.github.jhaucke.sms2mailforwarder;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jeremias.haucke on 04.05.2016.
 */
public class SendMailIntentService extends IntentService {

    public static final String SEND_MAIL_INTENT_SERVICE = "SendMailIntentService";

    private Handler toastHandler;
    private SharedPreferences sharedPreferences;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public SendMailIntentService() {
        super(SEND_MAIL_INTENT_SERVICE);
        toastHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * This method is invoked on the worker thread with a request to process.
     * Only one Intent is processed at a time, but the processing happens on a
     * worker thread that runs independently from other application logic.
     * So, if this code takes a long time, it will hold up other requests to
     * the same IntentService, but it will not hold up anything else.
     * When all requests have been handled, the IntentService stops itself,
     * so you should not call {@link #stopSelf}.
     *
     * @param intent The value passed to {@link
     *               Context#startService(Intent)}.
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Activity.MODE_PRIVATE);

        Mail m = new Mail(sharedPreferences.getString(Constants.SHARED_PREF_USER, ""), sharedPreferences.getString(Constants.SHARED_PREF_PASSWORD, ""),
                sharedPreferences.getString(Constants.SHARED_PREF_SMTP, ""));

        Set<String> stringSet = sharedPreferences.getStringSet(Constants.SHARED_PREF_RECIPIENTS, new HashSet<String>());
        m.setTo(stringSet.toArray(new String[stringSet.size()]));
        m.setFrom(sharedPreferences.getString(Constants.SHARED_PREF_FORWARDER, ""));
        m.setSubject(intent.getStringExtra(Constants.EXTRA_SUBJECT));
        m.setBody(intent.getStringExtra(Constants.EXTRA_BODY));

        try {
            if(m.send()) {
                toastHandler.post(new ToastRunnable(getString(R.string.mail_sent_successfully)));
            } else {
                toastHandler.post(new ToastRunnable(getString(R.string.mail_not_sent)));
            }
        } catch(Exception e) {
            toastHandler.post(new ToastRunnable(getString(R.string.mail_send_problem)));
            Log.e(SEND_MAIL_INTENT_SERVICE, getString(R.string.mail_could_not_send_email), e);
        }
    }

    private class ToastRunnable implements Runnable {
        String message;

        public ToastRunnable(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
