package com.example.handson;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SurveySTEMMajorSuitabilityActivity extends AppCompatActivity {

    TextView surveyType;
    TextView textPageNumber;
    Button nextButton;
    RecyclerView surveyRecyclerView;

    ArrayList<STEMRecyclerViewItem> arrayListQuestion;
    ArrayList<STEMRecyclerViewItem> subArrayList;
    STEMRecyclerViewAdapter recyclerViewAdapter;

    int totalPageNumber;
    int currentPageNumber = 1;
    int currentQuestionNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View surveyView = (View)getLayoutInflater().inflate(R.layout.activity_survey, null);

        surveyType = surveyView.findViewById(R.id.text_survey_type);
        textPageNumber = surveyView.findViewById(R.id.text_page_number);
        nextButton = surveyView.findViewById(R.id.button_survey_next);
        surveyRecyclerView = surveyView.findViewById(R.id.recycler_view_survey);
        arrayListQuestion = new ArrayList<STEMRecyclerViewItem>();

        Intent getIntent = getIntent();
        String questionJson = getIntent.getStringExtra("jsonResult");
        try {
            JSONArray questionArray = new JSONArray(questionJson);
            for(int idx =0;idx<questionArray.length();idx++){
                JSONObject questionObject = new JSONObject(String.valueOf(questionArray.getJSONObject(idx)));
                String question = questionObject.getString("question");

                boolean isSlash = false;
                for(int questionIndex = 0; questionIndex < question.length() - 1; questionIndex++){
                    if(question.charAt(questionIndex) == '/' & question.charAt(questionIndex + 1) == '>'){
                        StringBuffer questionBuffer = new StringBuffer();
                        questionIndex += 2;

                        while(questionIndex < question.length()){
                            questionBuffer.append(question.charAt(questionIndex));
                            questionIndex++;
                        }
                        arrayListQuestion.add(new STEMRecyclerViewItem(questionBuffer.toString()));
                        Log.d("DB_TAG", questionBuffer.toString());
                        isSlash = true;
                        break;
                    }
                }

                if(!isSlash){
                    arrayListQuestion.add(new STEMRecyclerViewItem(question));
                    Log.d("DB_TAG", question);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(arrayListQuestion.size() > 8) {
            currentQuestionNumber = 8;
        } else{
            currentQuestionNumber = arrayListQuestion.size();
        }
        totalPageNumber = arrayListQuestion.size()/8 + 1;

        subArrayList = new ArrayList<>(arrayListQuestion.subList(0, currentQuestionNumber));
        recyclerViewAdapter = new STEMRecyclerViewAdapter(subArrayList);
        surveyRecyclerView.setAdapter(recyclerViewAdapter);
        surveyRecyclerView.setLayoutManager(new LinearLayoutManager(SurveySTEMMajorSuitabilityActivity.this));

        surveyType.setText("이공계 전공 적합도 검사");
        textPageNumber.setText(currentPageNumber+"/"+totalPageNumber);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAllCheck = true;

                for(int questionNum=0;questionNum<subArrayList.size();questionNum++){
                    if(!subArrayList.get(questionNum).isCheckButton()) {
                        Toast.makeText(SurveySTEMMajorSuitabilityActivity.this, "누락된 문항이 있습니다. \n모든 문항에 체크해주세요.", Toast.LENGTH_SHORT).show();
                        isAllCheck = false;
                        break;
                    }
                }

                if(isAllCheck){
                    if(currentPageNumber == totalPageNumber){
                        String surveyResult = "";

                        for(int idx = 0;idx<arrayListQuestion.size();idx++){
                            surveyResult += ((idx+1)+"="+arrayListQuestion.get(idx).getCheckButtonNumber());

                            if(idx != arrayListQuestion.size()){
                                surveyResult += " ";
                            }
                        }

                        Intent intent = new Intent(SurveySTEMMajorSuitabilityActivity.this, SurveyResultActivity.class);
                        startActivity(intent);

                        finish();
                    } else{
                        int lastQuestionNumber = currentQuestionNumber;
                        if(currentQuestionNumber + 8 > arrayListQuestion.size()){
                            currentQuestionNumber = arrayListQuestion.size();
                        } else{
                            currentQuestionNumber += 8;
                        }

                        subArrayList = new ArrayList<STEMRecyclerViewItem>(arrayListQuestion.subList(lastQuestionNumber, currentQuestionNumber));
                        recyclerViewAdapter = new STEMRecyclerViewAdapter(subArrayList);
                        surveyRecyclerView.setAdapter(recyclerViewAdapter);
                        surveyRecyclerView.setLayoutManager(new LinearLayoutManager(SurveySTEMMajorSuitabilityActivity.this));

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
