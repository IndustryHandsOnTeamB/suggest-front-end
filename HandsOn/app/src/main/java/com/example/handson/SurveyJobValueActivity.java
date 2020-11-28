package com.example.handson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class SurveyJobValueActivity extends AppCompatActivity {

    TextView surveyType;
    TextView textPageNumber;
    Button nextButton;
    RecyclerView surveyRecyclerView;

    ArrayList<JobValueRecyclerViewItem> arrayListQuestion;
    ArrayList<JobValueRecyclerViewItem> subArrayList;
    JobValueRecyclerViewAdapter recyclerViewAdapter;

    int totalPageNumber;
    int currentPageNumber = 1;
    int currentQuestionNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View surveyView = (View)getLayoutInflater().inflate(R.layout.activity_survey,null);

        surveyType = surveyView.findViewById(R.id.text_survey_type);
        textPageNumber = surveyView.findViewById(R.id.text_page_number);
        nextButton = surveyView.findViewById(R.id.button_survey_next);
        surveyRecyclerView = surveyView.findViewById(R.id.recycler_view_survey);
        arrayListQuestion = new ArrayList<JobValueRecyclerViewItem>();

        //직업가치관 검사의 경우 받을 질문 ex) 1. 보수 / 자율성     2. 명예 / 보수
        //아래는 예시 input이며 실제로는 백엔드에서 받아와서 넣는 방식

        //예시 input
        arrayListQuestion.add(new JobValueRecyclerViewItem(new String[]{"능력발휘", "자율성"}));
        arrayListQuestion.add(new JobValueRecyclerViewItem(new String[]{"창의성", "안정성"}));
        arrayListQuestion.add(new JobValueRecyclerViewItem(new String[]{"보수", "창의성"}));
        arrayListQuestion.add(new JobValueRecyclerViewItem(new String[]{"안정성", "사회적인정"}));
        arrayListQuestion.add(new JobValueRecyclerViewItem(new String[]{"자기계발", "능력발휘"}));

        arrayListQuestion.add(new JobValueRecyclerViewItem(new String[]{"능력발휘", "자율성"}));
        arrayListQuestion.add(new JobValueRecyclerViewItem(new String[]{"능력발휘", "자율성"}));
        arrayListQuestion.add(new JobValueRecyclerViewItem(new String[]{"사회적인정", "보수"}));
        arrayListQuestion.add(new JobValueRecyclerViewItem(new String[]{"자율성", "사회적인정"}));
        arrayListQuestion.add(new JobValueRecyclerViewItem(new String[]{"능력발휘", "자율성"}));

        arrayListQuestion.add(new JobValueRecyclerViewItem(new String[]{"보수", "사회봉사"}));
        arrayListQuestion.add(new JobValueRecyclerViewItem(new String[]{"능력발휘", "자율성"}));
        arrayListQuestion.add(new JobValueRecyclerViewItem(new String[]{"보수", "사회봉사"}));
        arrayListQuestion.add(new JobValueRecyclerViewItem(new String[]{"사회봉사", "창의성"}));

        if(arrayListQuestion.size() > 10) {
            currentQuestionNumber = 10;
        } else{
            currentQuestionNumber = arrayListQuestion.size();
        }
        totalPageNumber = arrayListQuestion.size()/10 + 1;

        subArrayList = new ArrayList<>(arrayListQuestion.subList(0, currentQuestionNumber));
        recyclerViewAdapter = new JobValueRecyclerViewAdapter(subArrayList);
        surveyRecyclerView.setAdapter(recyclerViewAdapter);
        surveyRecyclerView.setLayoutManager(new LinearLayoutManager(SurveyJobValueActivity.this));

        surveyType.setText("직업 가치관 검사");
        textPageNumber.setText(currentPageNumber+"/"+totalPageNumber);

        //nextbutton 누르는 부분 미구현
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //boolean[] states = arrayListQuestion.get(2).getMstbStates();

                Log.d("TAG", "onClick: " + arrayListQuestion.get(2).getQuestion_text1());
                Log.d("TAG", "onClick: " + arrayListQuestion.get(2).getQuestion_text2());

//                int a = arrayListQuestion.get(2).getMstbValue(); - getState는 binding 하는 부분에서 하기
//                Log.d("TAG", "onClick: " + Integer.toString(a));

                //미체크된 항목 없는지 체크
                //미체크된 항목 있을 경우 - Toast띄우기
                //모두 체크되었다면 - 해당 페이지의 10개의 값을 저장 후 다음 페이지로 넘기기
            }
        });

        setContentView(surveyView);

    }
}
