package com.example.handson;

import android.content.Intent;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SurveyJobInterestActivity extends AppCompatActivity {

    TextView surveyType;
    TextView textPageNumber;
    Button nextButton;
    RecyclerView surveyRecyclerView;

    ArrayList<JobInterestRecyclerViewItem> arrayListQuestion;
    ArrayList<JobInterestRecyclerViewItem> subArrayList;
    JobInterestRecyclerViewAdapter recyclerViewAdapter;

    int totalPageNumber;
    int currentPageNumber = 1;
    int currentQuestionNumber;
    String userId, userName, userEmail, userType;
    int userPk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View surveyView = (View)getLayoutInflater().inflate(R.layout.activity_survey, null);

        surveyType = surveyView.findViewById(R.id.text_survey_type);
        textPageNumber = surveyView.findViewById(R.id.text_page_number);
        nextButton = surveyView.findViewById(R.id.button_survey_next);
        surveyRecyclerView = surveyView.findViewById(R.id.recycler_view_survey);
        arrayListQuestion = new ArrayList<JobInterestRecyclerViewItem>();

        Intent getIntent = getIntent();
        String questionJson = getIntent.getStringExtra("jsonResult");

        userId = getIntent.getStringExtra("userId");
        userName = getIntent.getStringExtra("userName");
        userEmail = getIntent.getStringExtra("userEmail");
        userType = getIntent.getStringExtra("userType");
        userPk = getIntent.getIntExtra("userPk", 4444);

        try {
            JSONArray questionArray = new JSONArray(questionJson);
            for(int idx =0;idx<questionArray.length();idx++){
                JSONObject questionObject = new JSONObject(String.valueOf(questionArray.getJSONObject(idx)));
                String question = questionObject.getString("question");
                question = question.replace("<br/>"," ");
                Log.d("DB_TAG", question);
                arrayListQuestion.add(new JobInterestRecyclerViewItem(question));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(arrayListQuestion.size() > 8) {
            currentQuestionNumber = 8;
        } else{
            currentQuestionNumber = arrayListQuestion.size();
        }
        totalPageNumber = arrayListQuestion.size()/8;

        subArrayList = new ArrayList<>(arrayListQuestion.subList(0, currentQuestionNumber));
        recyclerViewAdapter = new JobInterestRecyclerViewAdapter(subArrayList);
        surveyRecyclerView.setAdapter(recyclerViewAdapter);
        surveyRecyclerView.setLayoutManager(new LinearLayoutManager(SurveyJobInterestActivity.this));

        surveyType.setText("직업 흥미 검사");
        textPageNumber.setText(currentPageNumber+"/"+totalPageNumber);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAllCheck = true;

                for(int questionNum=0;questionNum<subArrayList.size();questionNum++){
                    if(!subArrayList.get(questionNum).isCheckButton()) {
                        Toast.makeText(SurveyJobInterestActivity.this, "누락된 문항이 있습니다. \n모든 문항에 체크해주세요.", Toast.LENGTH_SHORT).show();
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

                        JobInterestSurveyResultJson surveyResultJson = new JobInterestSurveyResultJson();
                        //String test = "1=4 2=4 3=3 4=4 5=1 6=1 7=2 8=3 9=4 10=3 11=1 12=4 13=1 14=1 15=3 16=2 17=2 18=1 19=3 20=4 21=3 22=1 23=1 24=1 25=1 26=1 27=1 28=1 29=2 30=3 31=1 32=1 33=1 34=1 35=3 36=3 37=2 38=3 39=2 40=1 41=1 42=1 43=1 44=3 45=4 46=1 47=1 48=3 49=3 50=1 51=1 52=1 53=1 54=1 55=3 56=3 57=1 58=1 59=4 60=2 61=2 62=1 63=1 64=2 65=3 66=1 67=1 68=1 69=3 70=4 71=1 72=1 73=1 74=1 75=1 76=1 77=1 78=1 79=1 80=1 81=1 82=1 83=1 84=1 85=1 86=1 87=1 88=1 89=1 90=1 91=1 92=1 93=1 94=1 95=1 96=1";
                        //surveyResultJson.execute(test);
                        surveyResultJson.execute(surveyResult);

                    } else{
                        int lastQuestionNumber = currentQuestionNumber;
                        if(currentQuestionNumber + 8 > arrayListQuestion.size()){
                            currentQuestionNumber = arrayListQuestion.size();
                        } else{
                            currentQuestionNumber += 8;
                        }

                        subArrayList = new ArrayList<JobInterestRecyclerViewItem>(arrayListQuestion.subList(lastQuestionNumber, currentQuestionNumber));
                        recyclerViewAdapter = new JobInterestRecyclerViewAdapter(subArrayList);
                        surveyRecyclerView.setAdapter(recyclerViewAdapter);
                        surveyRecyclerView.setLayoutManager(new LinearLayoutManager(SurveyJobInterestActivity.this));

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
    public class JobInterestSurveyResultJson extends AsyncTask<String, Void, String> {
        private int statusCode;
        public String doInBackground(String... params) {
            String jobValueResult = params[0];

            try {
                // answers result 정보를 Json object로 만들어서
                JSONObject myJsonObject = new JSONObject();
                try {
                    //myJsonObject에 key : answers, value에 String 형태의 jobValueResult 추가
                    Log.d("TAG", "doInBackground: "+jobValueResult);
                    myJsonObject.put("answer", jobValueResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String type;
                if(userType.equals("중학생")){
                    type="middle";
                }
                else{
                    type="high";
                }

                // 서버 api에 전송을 시도한다
                URL obj = new URL("http://15.165.18.48/api/v1/users/"+String.valueOf(userPk)+"/answer/"+ type +"/interest");

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

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                String line;
                StringBuilder sb = new StringBuilder();

                statusCode = conn.getResponseCode();

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

                        JobInteresetResultGetJson resultGetJson = new JobInteresetResultGetJson();
                        resultGetJson.execute(resultURL);
                    }

                } catch (JSONException e) {

                }
            }
        }
    }
    public class JobInteresetResultGetJson extends AsyncTask<String, Void, String> {
        private int statusCode;

        public String doInBackground(String... params) {
            String stemSurveyResultURL = params[0];
            Log.d("json1", "========================================================json함수 들어옴");

            try {
                // answers result 정보를 Json object로 만들어서
                JSONObject myJsonObject = new JSONObject();
                try {
                    //myJsonObject에 key : answers, value에 String 형태의 jobValueResult 추가
                    myJsonObject.put("url", stemSurveyResultURL);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // 서버 api에 전송을 시도한다
                URL obj = new URL("http://15.165.18.48/api/crawling/interest");

                HttpURLConnection conn = (HttpURLConnection) obj.openConnection(); // open connection

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                // 데이터를 읽어올 것이고
                conn.setDoInput(true);
                // 데이터를 쓸 것이다.
                conn.setDoOutput(true);

                // property 지정해주고
                conn.setRequestProperty("Accept-Charset", "UTF-8");
                conn.setRequestProperty("Content-Type", "application/json");

                // 전송을 해본다
                OutputStream os = conn.getOutputStream();
                os.write(myJsonObject.toString().getBytes());
                os.flush();
                os.close();
                Log.d("json1", "========================================================json보냄");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                String line;
                StringBuilder sb = new StringBuilder();

                statusCode = conn.getResponseCode();
                Log.d("json1", "========================================================스테이터스코드 받음");

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
                Log.d("json1", "========================================================null");
                Toast.makeText(getApplicationContext(), "정보가 잘못되었습니다.", Toast.LENGTH_SHORT).show();

            } else {
                try {
                    // 수신한 data s에 대해
                    JSONObject jsonObject = new JSONObject(s);
                    if (statusCode == HttpURLConnection.HTTP_OK || statusCode == HttpURLConnection.HTTP_CREATED) {
                        // 데이터들을 추출하여 변수에 저장한다.
                        JSONObject resultObject = jsonObject.getJSONObject("result");

                        String result = resultObject.toString();
                        Log.d("json1",result);

                        String jobType = new String();
                        String jobList = new String();

                        for(int idx = 0; idx <result.length(); idx++){
                            if(result.charAt(idx)=='1' | result.charAt(idx)=='2' | result.charAt(idx)=='3'){
                                Log.d("json1", "========================================================job type 저장");
                                idx += 2;
                                if(result.charAt(idx)==' '){
                                    idx++;
                                }
                                StringBuffer jobTypeBuffer = new StringBuffer();
                                while(true){
                                    if(result.charAt(idx)=='"'){
                                        idx++;
                                        break;
                                    }
                                    jobTypeBuffer.append(result.charAt(idx++));
                                }
                                String jobResult = jobTypeBuffer.toString();
                                jobResult = jobResult.replaceAll("\\\\","");
                                jobResult += ", ";
                                jobType += jobResult;
                            }

                            if(result.charAt(idx)=='['){
                                idx++;

                                Log.d("json1", "========================================================job list 저장");
                                StringBuffer jobBuffer = new StringBuffer();
                                int listSize = 0;
                                while(true){
                                    if(listSize>2){
                                        break;
                                    }
                                    if(result.charAt(idx)==']'){
                                        idx++;
                                        break;
                                    }
                                    if(result.charAt(idx)=='"'){
                                        idx++;
                                        continue;
                                    }
                                    if(result.charAt(idx)==',') {
                                        listSize++;
                                        jobBuffer.append(", ");
                                        idx++;
                                        continue;
                                    }
                                    jobBuffer.append(result.charAt(idx++));
                                }
                                jobList += jobBuffer.toString();
                            }
                        }

                        Log.d("json1", "========================================================변수저장");
                        jobType = jobType.substring(0, jobType.length()-2);
                        Log.d("json1","type = "+ jobType);
                        jobList = jobList.substring(0, jobList.length()-2);
                        Log.d("json1", "list = "+ jobList);

                        Intent intent = new Intent(SurveyJobInterestActivity.this, SurveyResultActivity.class);
                        intent.putExtra("type", "0");
                        intent.putExtra("jobType", jobType);
                        intent.putExtra("jobList", jobList);
                        intent.putExtra("userId", userId);
                        intent.putExtra("userName", userName);
                        intent.putExtra("userEmail", userEmail);
                        intent.putExtra("userType", userType);
                        intent.putExtra("userPk", userPk);
                        startActivity(intent);
                        finish();
                    }

                } catch (JSONException e) {
                    Log.d("json1", e.getMessage());
                }
            }
        }
    }
}
