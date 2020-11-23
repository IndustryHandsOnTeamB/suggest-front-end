package com.example.handson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuSelect extends AppCompatActivity {


    Button lastResultButton;
    Button counselCasesButton;
    Button testStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_select);

        lastResultButton = (Button)findViewById(R.id.last_result_button);
        counselCasesButton = (Button)findViewById(R.id.counsel_cases_button);
        testStartButton = (Button) findViewById(R.id.test_start_button);

        lastResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PastResultActivity.class);
                startActivity(intent);
            }
        });

        counselCasesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CaseSearchActivity.class);
                startActivity(intent);
            }
        });

        testStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), #####.class);
//                startActivity(intent);
            }
        });

    }
}
