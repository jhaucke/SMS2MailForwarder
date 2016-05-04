package com.github.jhaucke.sms2mailforwarder;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by jeremias.haucke on 04.05.2016.
 */
public class SendMailIntentService extends IntentService {

    private Handler toastHandler;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public SendMailIntentService() {
        super("SendMailIntentService");
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
        Mail m = new Mail("gmailusername@gmail.com", "password");

        String[] toArr = {"bla@bla.com", "lala@lala.com"};
        m.setTo(toArr);
        m.setFrom("wooo@wooo.com");
        m.setSubject("This is an email sent using my Mail JavaMail wrapper from an Android device.");
        m.setBody("Email body.");

        try {
            if(m.send()) {
                toastHandler.post(new ToastRunnable("Email was sent successfully."));
            } else {
                toastHandler.post(new ToastRunnable("Email was not sent."));
            }
        } catch(Exception e) {
            toastHandler.post(new ToastRunnable("There was a problem sending the email."));
            Log.e("SendMailIntentService", "Could not send email", e);
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
