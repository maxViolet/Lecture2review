package com.example.android.maximfialko;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_LONG;

public class previewEmailActivity extends AppCompatActivity {

    ActionBar actionBar;
    public static final String EXTRA_MESSAGE = "message";
    public static final String SUBJECT = "homework";

    public static void start(Activity activity, String messageText) {
        Intent intent = new Intent(activity, previewEmailActivity.class);
        intent.putExtra(EXTRA_MESSAGE, messageText);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_email_activity);

        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F0AC73")));

        final Button email = (Button) findViewById(R.id.emailButton);
        final TextView messageView = (TextView) findViewById(R.id.textOutput);

        //получить текст письма через Extras и заполнить соотв-й TextView
        final String message = getIntent().getStringExtra(EXTRA_MESSAGE);
        messageView.setText(message);

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEmailApp(message);
            }
        });
    }

    //открытие почты по кнопке EMAIL
    public void openEmailApp(String BODY) {
        final Intent sendEmail = new Intent(Intent.ACTION_SENDTO);
        sendEmail.setData(Uri.parse(String.format("mailto:%s", getString(R.string.email))));
        sendEmail.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
        sendEmail.putExtra(Intent.EXTRA_TEXT, BODY);

        if (sendEmail.resolveActivity(getPackageManager()) != null) {
            startActivity(sendEmail);
        } else {
            Toast.makeText(this, R.string.no_email_app_found, LENGTH_LONG).show();
        }
    }
}
