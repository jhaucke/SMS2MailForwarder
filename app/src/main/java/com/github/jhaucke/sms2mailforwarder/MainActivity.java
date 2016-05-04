package com.github.jhaucke.sms2mailforwarder;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Main Activity
 *
 * inspired by: http://www.jondev.net/articles/Sending_Emails_without_User_Intervention_%28no_Intents%29_in_Android
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.btn_send_test_mail);

        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startService(new Intent(getApplicationContext(), SendMailIntentService.class));
            }
        });
    }
}
