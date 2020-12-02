package com.example.handson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import static com.example.handson.MenuSelect.userPk;

public class SurveyJobValueActivity extends AppCompatActivity {

    //int userPk;
    //String userType;

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

                        //Intent intent = new Intent(SurveyJobValueActivity.this, SurveyResultActivity.class);



                        // 결과 string으로 post method로 보내기
                        SurveyResultJson surveyResultJson = new SurveyResultJson();

                        String testResult="B1=1 B2=1 B3=1 B4=1 B5=1 B6=1 B7=1 B8=5 B9=5 B10=1 B11=4 B12=4 B13=5 B14=4 B15=4 B16=4 B17=4 B18=5 B19=1 B20=1 B21=1 B22=5 B23=3 B24=6 B25=3 B26=2 B27=2 B28=1";
                        surveyResultJson.execute(testResult);
                        //surveyResultJson.execute(surveyResult);

                        //startActivity(intent);
                        //finish();
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


    //json object send & receive
    public class SurveyResultJson extends AsyncTask<String, Void, String> {
        private int statusCode;
        public String doInBackground(String... params) {
            String jobValueResult = params[0];
            Log.d("json","========================================================json함수 들어옴");

            try {
                // answers result 정보를 Json object로 만들어서
                JSONObject myJsonObject = new JSONObject();
                try {
                    //myJsonObject에 key : answers, value에 String 형태의 jobValueResult 추가
                    myJsonObject.put("answer", jobValueResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                // 서버 api에 전송을 시도한다
                URL obj = new URL("http://15.165.18.48/api/v1/users/"+String.valueOf(userPk)+"/answer/common/value");

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
                Log.d("TAG","========================================================null");
                Toast.makeText(getApplicationContext(),"정보가 잘못되었습니다.",Toast.LENGTH_SHORT).show();

            } else {
                try {
                    // 수신한 data s에 대해
                    JSONObject jsonObject = new JSONObject(s);

                    if(statusCode == 200){
                        // 데이터들을 추출하여 변수에 저장한다.
                        String resultURL;
                        resultURL = jsonObject.get("url").toString();
                        Log.d("TAG","==============================STATUS 200===================");
                        Log.d("TAG", "onPostExecute: url is  " + resultURL);


                        Intent intent = new Intent(SurveyJobValueActivity.this, SurveyResultActivity.class);
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
