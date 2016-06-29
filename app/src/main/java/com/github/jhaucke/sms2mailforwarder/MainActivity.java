package com.github.jhaucke.sms2mailforwarder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Main Activity
 *
 * inspired by: http://www.jondev.net/articles/Sending_Emails_without_User_Intervention_%28no_Intents%29_in_Android
 */
public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private EditText etRecipients;
    private EditText etForwarder;
    private EditText etUser;
    private EditText etPassword;
    private EditText etSmtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        etRecipients   = (EditText)findViewById(R.id.et_recipients);
        etForwarder   = (EditText)findViewById(R.id.et_forwarder);
        etUser   = (EditText)findViewById(R.id.et_user);
        etPassword   = (EditText)findViewById(R.id.et_password);
        etSmtp   = (EditText)findViewById(R.id.et_smtp);

        restorePreferences();

        Button button = (Button) findViewById(R.id.btn_send_test_mail);

        assert button != null;

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                savePreferences();

                Intent intent = new Intent(getApplicationContext(), SendMailIntentService.class);
                intent.putExtra(Constants.EXTRA_SUBJECT, getString(R.string.default_mail_subject));
                intent.putExtra(Constants.EXTRA_BODY, getString(R.string.default_mail_body));
                startService(intent);
            }
        });
    }

    private void restorePreferences() {
        Set<String> recipientsSet = sharedPreferences.getStringSet(Constants.SHARED_PREF_RECIPIENTS, new HashSet<String>());
        StringBuilder recipients = new StringBuilder("");
        int recipientsCount = 1;
        for (String recipient : recipientsSet) {
            recipients.append(recipient);
            if (recipientsSet.size() > recipientsCount) {
                recipients.append(";");
            }
            recipientsCount++;
        }
        if (!recipients.toString().isEmpty()) {
            etRecipients.setText(recipients);
        }
        String forwarder = sharedPreferences.getString(Constants.SHARED_PREF_FORWARDER, null);
        if (forwarder != null) {
            etForwarder.setText(forwarder);
        }
        String user = sharedPreferences.getString(Constants.SHARED_PREF_USER, null);
        if (user != null) {
            etUser.setText(user);
        }
        String password = sharedPreferences.getString(Constants.SHARED_PREF_PASSWORD, null);
        if (password != null) {
            etPassword.setText(password);
        }
        String smtp = sharedPreferences.getString(Constants.SHARED_PREF_SMTP, null);
        if (smtp != null) {
            etSmtp.setText(smtp);
        }
    }

    private void savePreferences() {
        if (etRecipients.getText() != null) {
            editor.putStringSet(Constants.SHARED_PREF_RECIPIENTS, new HashSet<String>(Arrays.asList(etRecipients.getText().toString().split(";"))));
        }
        if (etForwarder.getText() != null) {
            editor.putString(Constants.SHARED_PREF_FORWARDER, etForwarder.getText().toString());
        }
        if (etUser.getText() != null) {
            editor.putString(Constants.SHARED_PREF_USER, etUser.getText().toString());
        }
        if (etPassword.getText() != null) {
            editor.putString(Constants.SHARED_PREF_PASSWORD, etPassword.getText().toString());
        }
        if (etSmtp.getText() != null) {
            editor.putString(Constants.SHARED_PREF_SMTP, etSmtp.getText().toString());
        }
        editor.apply();
    }
}
