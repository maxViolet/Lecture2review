package com.example.android.maximfialko;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button previewEmail = findViewById(R.id.previewEmailButton);
        final EditText textInput = findViewById(R.id.textInput);

        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F0AC73")));

/*        Toolbar toolBar = (Toolbar) findViewById(R.id.activity_my_toolbar);
        setSupportActionBar(toolBar);*/

        previewEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSecondActivity(textInput.getText().toString());
            }
        });
    }

    public void openSecondActivity(String text) {
        previewEmailActivity.start(this, text);
    }

}
