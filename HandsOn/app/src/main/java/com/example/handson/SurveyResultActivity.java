package com.example.handson;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SurveyResultActivity extends AppCompatActivity {

    Button moveMainMenuButton;
    TextView resultText1;
    TextView resultText2;

    ExpandableListView mbtiListView;
    ArrayList<String> mbtiRecommendJob;
    String mbtiRecommendText = "";

    /*
    설문 진행 후 결과창으로 넘어올 때 설문 타입을 넘겨받음
    0 : 직업 흥미 검사
    1 : 직업 적성 검사
    2 : 직업 가치관 검사
    3 : 이공계 적합도 검사
    */
    int surveyType = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_result);

        moveMainMenuButton = findViewById(R.id.button_main_menu);
        mbtiListView = (ExpandableListView) findViewById(R.id.expandable_list_view_mbti_job);

        LinearLayout surveyResultLayout = (LinearLayout)findViewById(R.id.layout_survey_result_dynamic);
        ResultItem resultItem;

        /*
        Intent getIntent = getIntent();
        surveyType = intent.getIntExtra("surveyType", 4444);
        */

        switch (surveyType){
            case 0:
                resultItem = new ResultJobInterestItem(this);
                resultText1 = findViewById(R.id.text_job_interest_result1);
                resultText2 = findViewById(R.id.text_job_interest_result2);
                break;
            case 1:
                resultItem = new ResultJobAptitudeItem(this);
                resultText1 = findViewById(R.id.text_job_apt_result1);
                resultText2 = findViewById(R.id.text_job_apt_result2);
                break;
            case 2:
                resultItem = new ResultJobValueItem(this);
                resultText1 = findViewById(R.id.text_job_value_result1);
                resultText2 = findViewById(R.id.text_job_value_result2);
                break;
            case 3:
                resultItem = new ResultSTEMItem(this);
                resultText1 = findViewById(R.id.text_stem_result1);
                resultText2 = findViewById(R.id.text_stem_result2);
                break;
            default:
                resultItem = new ResultItem(this);
        }

        surveyResultLayout.addView(resultItem);

        //결과 url에서 파싱한 결과 설정
        //resultText1.setText("");
        //resultText2.setText("");

        // mbti 직업 리스트 GET
        mbtiRecommendJob = new ArrayList<>();

        // 임시로 데이터 저장
        mbtiRecommendJob.add("경영 컨설턴트");
        mbtiRecommendJob.add("제약회사 연구원");
        mbtiRecommendJob.add("분석가");
        mbtiRecommendJob.add("회계사");
        mbtiRecommendJob.add("시스템 개발");

        for(int idx = 0;idx < mbtiRecommendJob.size();idx++){
            mbtiRecommendText += mbtiRecommendJob.get(idx);

            if(idx < mbtiRecommendJob.size()-1){
                mbtiRecommendText += ", ";
            }
        }

        MbtiListViewAdapter mbtiListViewAdapter = new MbtiListViewAdapter(getApplicationContext(), R.layout.mbti_title_item, R.layout.mbti_job_list_item, mbtiRecommendText);
        mbtiListView.setAdapter(mbtiListViewAdapter);

        moveMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SurveyResultActivity.this, MenuSelect.class);
                startActivity(intent);
            }
        });
    }
}
