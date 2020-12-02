package com.example.handson;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.example.handson.MenuSelect.userPk;

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

        // 임시로 질문 넣어둠
        // 이후 백엔드와 통신 후 질문 리스트 받아야 함
        arrayListQuestion.add(new STEMRecyclerViewItem("1"));
        arrayListQuestion.add(new STEMRecyclerViewItem("2"));
        arrayListQuestion.add(new STEMRecyclerViewItem("3"));
        arrayListQuestion.add(new STEMRecyclerViewItem("4"));
        arrayListQuestion.add(new STEMRecyclerViewItem("5"));
        arrayListQuestion.add(new STEMRecyclerViewItem("6"));
        arrayListQuestion.add(new STEMRecyclerViewItem("7"));
        arrayListQuestion.add(new STEMRecyclerViewItem("8"));
        arrayListQuestion.add(new STEMRecyclerViewItem("9"));
        arrayListQuestion.add(new STEMRecyclerViewItem("10"));
        arrayListQuestion.add(new STEMRecyclerViewItem("11"));
        arrayListQuestion.add(new STEMRecyclerViewItem("12"));

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

                        STEMSurveyResultJson surveyResultJson = new STEMSurveyResultJson();
                        String testResult;
                        testResult="1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2";
                        //surveyResultJson.execute(surveyResult);
                        surveyResultJson.execute(testResult);

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


    //json object send & receive
    public class STEMSurveyResultJson extends AsyncTask<String, Void, String> {
        private int statusCode;
        public String doInBackground(String... params) {
            String stemSurveyResult = params[0];
            Log.d("json","========================================================json함수 들어옴");

            try {
                // answers result 정보를 Json object로 만들어서
                JSONObject myJsonObject = new JSONObject();
                try {
                    //myJsonObject에 key : answers, value에 String 형태의 jobValueResult 추가
                    myJsonObject.put("answer", stemSurveyResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                // 서버 api에 전송을 시도한다
                URL obj = new URL("http://15.165.18.48/api/v1/users/"+String.valueOf(userPk)+"/answer/common/se");

                HttpURLConnection conn = (HttpURLConnection) obj.openConnection(); // open connection

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                // 데이터를 읽어올 것이고
                conn.setDoInput(true);
                // 데이터를 쓸 것이다.
                conn.setDoOutput(true);

                // property 지정해주고
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-Type", "application/json");

                // 전송을 해본다
                OutputStream os = conn.getOutputStream();
                os.write(myJsonObject.toString().getBytes());
                os.flush();
                os.close();
                Log.d("json","========================================================json보냄");



                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                String line;
                StringBuilder sb = new StringBuilder();

                statusCode = conn.getResponseCode();
                Log.d("json","========================================================스테이터스코드 받음");

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();
                return sb.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s == null) {
                // 서버에서 널 값이 온경우. API가 이상하거나. 서버가 꺼져있는 경우
                Log.d("json","========================================================null");
                Toast.makeText(getApplicationContext(),"정보가 잘못되었습니다.",Toast.LENGTH_SHORT).show();

            } else {
                try {
                    // 수신한 data s에 대해
                    JSONObject jsonObject = new JSONObject(s);

                    if(statusCode == 200){
                        // 데이터들을 추출하여 변수에 저장한다.
                        String resultURL;
                        resultURL = jsonObject.get("url").toString();
                        Log.d("json","========================================================변수저장");

                        Intent intent = new Intent(SurveySTEMMajorSuitabilityActivity.this, SurveyResultActivity.class);
                        intent.putExtra("resultURL", resultURL);
                        startActivity(intent);
                        finish();
                    }

                } catch (JSONException e) {

                }
            }
        }

    }
}
