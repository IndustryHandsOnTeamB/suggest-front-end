package com.example.handson;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

                //풀지 않은 문항이 있는지 체크
                for(int qNum = 0 ; qNum < subArrayList.size(); qNum++){
                    if(subArrayList.get(qNum).getMstbVal() == -1){
                        Toast.makeText(SurveyJobAptitudeActivity.this, "누락된 문항이 있습니다. \n모든 문항에 체크해주세요.", Toast.LENGTH_SHORT).show();
                        isAllCheck = false;
                        break;
                    }
                }

                if(isAllCheck){
                    if(currentPageNumber == totalPageNumber){
                        //surveyResult에 설문에 대한 답변들을 이어서 저장 -> 이후에 백엔드로 전송
                        String surveyResult = "";

                        for(int idx = 0 ; idx<arrayListQuestion.size();idx++){
                            surveyResult += ((idx+1)+"="+((arrayListQuestion.get(idx).getMstbVal()+1)));

                            if(idx!= arrayListQuestion.size()){
                                surveyResult+=" ";
                            }
                        }
                        Log.d("HS_TAG", "onClick: "+surveyResult);

                        Intent intent = new Intent(SurveyJobAptitudeActivity.this, SurveyResultActivity.class);
                        startActivity(intent);

                        finish();
                    }
                    else{
                        int lastQuestionNumber = currentQuestionNumber;
                        if(currentQuestionNumber + 8 > arrayListQuestion.size()){
                            currentQuestionNumber = arrayListQuestion.size();
                        }
                        else{
                            currentQuestionNumber += 8;
                        }

                        subArrayList = new ArrayList<JobAptitudeRecyclerViewItem>(arrayListQuestion.subList(lastQuestionNumber, currentQuestionNumber));
                        recyclerViewAdapter = new JobAptitudeRecyclerViewAdapter(subArrayList);
                        surveyRecyclerView.setAdapter(recyclerViewAdapter);
                        surveyRecyclerView.setLayoutManager(new LinearLayoutManager(SurveyJobAptitudeActivity.this));

                        currentPageNumber++;
                        if(currentPageNumber == totalPageNumber){
                            nextButton.setText("END");
                        }
                        textPageNumber.setText(currentPageNumber+"/"+totalPageNumber);

                    }
                }

            }
        });


        setContentView(surveyView);
    }




}
