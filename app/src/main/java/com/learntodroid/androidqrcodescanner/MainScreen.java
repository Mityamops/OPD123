package com.learntodroid.androidqrcodescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);


        Button button=findViewById(R.id.button);
        Button button_test=findViewById(R.id.button_test);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainScreen.this,MainActivity.class);
                        startActivity(intent);
                    }
                }
        );
        button_test.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainScreen.this,Photo_activity.class);
                        startActivity(intent);
                    }
                }
        );
    }
}

