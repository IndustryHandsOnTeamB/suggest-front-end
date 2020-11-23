package com.example.handson;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SurveyResultActivity extends AppCompatActivity {

    TextView surveyResultType;
    TextView surveyResult;
    Button moveMainMenuButton;
    Button recommendListButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_result);

        surveyResultType = findViewById(R.id.text_survey_result_type);
        surveyResult = findViewById(R.id.text_survey_result);
        moveMainMenuButton = findViewById(R.id.button_main_menu);
        recommendListButton = findViewById(R.id.button_recommend_list);

        // 우선 토스트 메시지 띄우는 걸로 설정
        moveMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SurveyResultActivity.this, "메인 메뉴로 이동", Toast.LENGTH_SHORT).show();
            }
        });

        recommendListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SurveyResultActivity.this, RecommendListActivity.class);
                startActivity(intent);
            }
        });
    }
}
