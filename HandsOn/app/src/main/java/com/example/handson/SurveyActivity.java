package com.example.handson;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class SurveyActivity extends AppCompatActivity {

    TextView surveyType;
    Button nextButton;
    RecyclerView surveyRecyclerView;
    // RecyclerView 어댑터를 추가해 연결해야함

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        surveyType = findViewById(R.id.text_survey_type);
        nextButton = findViewById(R.id.button_survey_next);
        surveyRecyclerView = findViewById(R.id.recycler_view_survey);

        //우선 바로 결과 화면으로 넘어가도록 설정
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SurveyActivity.this, SurveyResultActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }
}
