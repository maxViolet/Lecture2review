package com.example.android.maximfialko.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.maximfialko.R;

public class PreviewEmailActivity extends AppCompatActivity {

    private static final String EXTRA_MESSAGE = "message";
    private static final String SUBJECT = "homework";

    public static void start(Activity activity, String messageText) {
        Intent intent = new Intent(activity, PreviewEmailActivity.class);
        intent.putExtra(EXTRA_MESSAGE, messageText);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_activity);

        final Button email = findViewById(R.id.emailButton);
        final TextView messageView = findViewById(R.id.textOutput);

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
    private void openEmailApp(String BODY) {
        final Intent sendEmail = new Intent(Intent.ACTION_SENDTO);
        sendEmail.setData(Uri.parse(String.format("mailto:%s", getString(R.string.email))));
        sendEmail.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
        sendEmail.putExtra(Intent.EXTRA_TEXT, BODY);

        if (sendEmail.resolveActivity(getPackageManager()) != null) {
            startActivity(sendEmail);
        } else {
            Toast.makeText(this, R.string.no_email_app_found, Toast.LENGTH_LONG).show();
        }
    }
}
