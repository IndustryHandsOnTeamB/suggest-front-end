package com.example.handson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuSelect extends AppCompatActivity {

    String userId, userName, userEmail, userType;
    int userPK;

    Button lastResultButton;
    Button mbtiButton;
    Button testStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_select);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        userName = intent.getStringExtra("userName");
        userEmail = intent.getStringExtra("userEmail");
        userType = intent.getStringExtra("userType");
        userPK = intent.getIntExtra("userPK", 4444);

        lastResultButton = (Button)findViewById(R.id.last_result_button);
        mbtiButton = (Button)findViewById(R.id.mbti_button);
        testStartButton = (Button) findViewById(R.id.test_start_button);

        lastResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PastResultActivity.class);
                startActivity(intent);
            }
        });

        mbtiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MbtiActivity.class);
                startActivity(intent);
            }
        });

        testStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StartNewSurveyActivity.class);
                startActivity(intent);
            }
        });

    }
}
