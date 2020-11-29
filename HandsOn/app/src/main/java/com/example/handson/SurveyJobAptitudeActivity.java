package com.example.handson;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SurveyJobAptitudeActivity extends AppCompatActivity {

    TextView surveyType;
    TextView textPageNumber;
    Button nextButton;
    RecyclerView surveyRecyclerView;

    ArrayList<JobAptitudeRecyclerViewItem> arrayListQuestion;
    ArrayList<JobAptitudeRecyclerViewItem> subArrayList;
    JobAptitudeRecyclerViewAdapter recyclerViewAdapter;

    int totalPageNumber;
    int currentPageNumber = 1;
    int currentQuestionNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View surveyView = (View) getLayoutInflater().inflate(R.layout.activity_survey, null);

        surveyType = surveyView.findViewById(R.id.text_survey_type);
        textPageNumber = surveyView.findViewById(R.id.text_page_number);
        nextButton = surveyView.findViewById(R.id.button_survey_next);
        surveyRecyclerView = surveyView.findViewById(R.id.recycler_view_survey);
        arrayListQuestion = new ArrayList<JobAptitudeRecyclerViewItem>();
        Log.d("TAG", "onCreate: " + Integer.toString(arrayListQuestion.size()));

        //임시 input
        arrayListQuestion.add(new JobAptitudeRecyclerViewItem("1"));
        arrayListQuestion.add(new JobAptitudeRecyclerViewItem("2"));
        arrayListQuestion.add(new JobAptitudeRecyclerViewItem("3"));
        arrayListQuestion.add(new JobAptitudeRecyclerViewItem("4"));
        arrayListQuestion.add(new JobAptitudeRecyclerViewItem("5"));
        arrayListQuestion.add(new JobAptitudeRecyclerViewItem("6"));
        arrayListQuestion.add(new JobAptitudeRecyclerViewItem("7"));
        arrayListQuestion.add(new JobAptitudeRecyclerViewItem("8"));
        arrayListQuestion.add(new JobAptitudeRecyclerViewItem("9"));
        arrayListQuestion.add(new JobAptitudeRecyclerViewItem("10"));
        arrayListQuestion.add(new JobAptitudeRecyclerViewItem("11"));
        arrayListQuestion.add(new JobAptitudeRecyclerViewItem("12"));
        Log.d("TAG", "onCreate: " + Integer.toString(arrayListQuestion.size()));


        if(arrayListQuestion.size() > 8) {
            currentQuestionNumber = 8;
        } else{
            currentQuestionNumber = arrayListQuestion.size();
        }
        totalPageNumber = arrayListQuestion.size()/8 + 1;

        subArrayList = new ArrayList<>(arrayListQuestion.subList(0, currentQuestionNumber));
        recyclerViewAdapter = new JobAptitudeRecyclerViewAdapter(subArrayList);
        surveyRecyclerView.setAdapter(recyclerViewAdapter);
        surveyRecyclerView.setLayoutManager(new LinearLayoutManager(SurveyJobAptitudeActivity.this));
        recyclerViewAdapter.notifyDataSetChanged();


        surveyType.setText("직업 적성 검사");
        textPageNumber.setText(currentPageNumber+"/"+totalPageNumber);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isAllCheck = true;

                for(int qNum = 0; qNum < subArrayList.size(); qNum++){
                    if(subArrayList.get(qNum).getMstbValue() != -1){
                        qNum++;
                    }
                    else{

                    }
                }
                //미체크된 항목 없는지 체크
                //미체크된 항목 있을 경우 - Toast띄우기
                //모두 체크되었다면 - 해당 페이지의 10개의 값을 저장 후 다음 페이지로 넘기기
            }
        });


        setContentView(surveyView);
    }




}
