package com.example.handson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

        if(arrayListQuestion.size() > 7) {
            currentQuestionNumber = 7;
        } else{
            currentQuestionNumber = arrayListQuestion.size();
        }
        totalPageNumber = arrayListQuestion.size()/7 + 1;

        subArrayList = new ArrayList<>(arrayListQuestion.subList(0, currentQuestionNumber));
        recyclerViewAdapter = new JobValueRecyclerViewAdapter(subArrayList);
        surveyRecyclerView.setAdapter(recyclerViewAdapter);
        surveyRecyclerView.setLayoutManager(new LinearLayoutManager(SurveyJobValueActivity.this));

        surveyType.setText("직업 가치관 검사");
        textPageNumber.setText(currentPageNumber+"/"+totalPageNumber);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAllCheck = true;

                //풀지 않은 문항이 있는지 체크
                for(int qNum = 0 ; qNum < subArrayList.size(); qNum++){
                    if(subArrayList.get(qNum).getMstbVal() == -1){
                        Toast.makeText(SurveyJobValueActivity.this, "누락된 문항이 있습니다. \n모든 문항에 체크해주세요.", Toast.LENGTH_SHORT).show();
                        isAllCheck = false;
                        break;
                    }
                }

                if(isAllCheck){
                    if(currentPageNumber == totalPageNumber){
                        //surveyResult에 설문에 대한 답변들을 이어서 저장 -> 이후에 백엔드로 전송
                        String surveyResult = "";

                        for(int idx = 0 ; idx<arrayListQuestion.size();idx++){

                            //jobValueTest의 경우 1번의 답 선지 1,2 / 2번은 3,4 / 3번은 5,6 / ...
                            surveyResult += ("B"+(idx+1)+"="+((arrayListQuestion.get(idx).getMstbVal()+1)+(2*idx)));

                            if(idx!= arrayListQuestion.size()){
                                surveyResult+=" ";
                            }
                        }
                        Log.d("HS_TAG", "onClick: "+surveyResult);

                        Intent intent = new Intent(SurveyJobValueActivity.this, SurveyResultActivity.class);
                        startActivity(intent);

                        finish();
                    }
                    else{
                        int lastQuestionNumber = currentQuestionNumber;
                        if(currentQuestionNumber + 7 > arrayListQuestion.size()){
                            currentQuestionNumber = arrayListQuestion.size();
                        }
                        else{
                            currentQuestionNumber += 7;
                        }

                        subArrayList = new ArrayList<JobValueRecyclerViewItem>(arrayListQuestion.subList(lastQuestionNumber, currentQuestionNumber));
                        recyclerViewAdapter = new JobValueRecyclerViewAdapter(subArrayList);
                        surveyRecyclerView.setAdapter(recyclerViewAdapter);
                        surveyRecyclerView.setLayoutManager(new LinearLayoutManager(SurveyJobValueActivity.this));

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
