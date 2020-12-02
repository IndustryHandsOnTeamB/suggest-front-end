package com.example.handson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MenuSelect extends AppCompatActivity {

    Button lastResultButton;
    Button mbtiButton;
    Button testStartButton;

    String userId, userName, userEmail;
    public static String userType;
    public static int userPk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_select);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        userName = intent.getStringExtra("userName");
        userEmail = intent.getStringExtra("userEmail");
        userType = intent.getStringExtra("userType");
        userPk = intent.getIntExtra("userPk",4444);

        lastResultButton = (Button)findViewById(R.id.last_result_button);
        mbtiButton = (Button)findViewById(R.id.mbti_button);
        testStartButton = (Button) findViewById(R.id.test_start_button);

        lastResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PastResultActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("userName", userName);
                intent.putExtra("userEmail", userEmail);
                intent.putExtra("userType", userType);
                intent.putExtra("userPk", userPk);
                startActivity(intent);
            }
        });

        mbtiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(getApplicationContext(), MbtiActivity.class);
                intent3.putExtra("userPk", userPk);
                startActivity(intent3);
            }
        });

        testStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StartNewSurveyActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("userName", userName);
                intent.putExtra("userEmail", userEmail);
                intent.putExtra("userType", userType);
                intent.putExtra("userPk", userPk);
                startActivity(intent);
//                Intent intent = new Intent(getApplicationContext(), SurveyJobInterestActivity.class);
//                startActivity(intent);
            }
        });

    }
}
