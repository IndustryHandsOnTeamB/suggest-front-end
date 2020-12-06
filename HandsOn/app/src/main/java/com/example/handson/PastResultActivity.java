package com.example.handson;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PastResultActivity extends AppCompatActivity {

    public static PastResultActivity pastResultActivity;

    public ListView listview_pastResult;
    ArrayList<PastResult_listview> arrayList = new ArrayList<>();
    PastResult_listviewadapter adapter_pastResult = new PastResult_listviewadapter(arrayList, this);

    Button tempBtn;

    private String userId, userName, userEmail, userType, surveyType, requestHistoryUrl;
    private int userPk, surveyTypeInt;

    private String listviewSelectedUrl="";

    JSONArray urlJsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_result);

        pastResultActivity = PastResultActivity.this;

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        userName = intent.getStringExtra("userName");
        userEmail = intent.getStringExtra("userEmail");
        userType = intent.getStringExtra("userType");
        userPk = intent.getIntExtra("userPk", 4444);

        Log.d("###userPk", Integer.toString(userPk));

        requestHistoryUrl = "http://15.165.18.48/api/v1/users/" + userPk + "/testhistory";

        listview_pastResult = (ListView) findViewById(R.id.listview_pastresult);
        listview_pastResult.setAdapter(adapter_pastResult);

        listview_pastResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    listviewSelectedUrl = urlJsonArray.get(position).toString();
                    tempBtn.setEnabled(true);

                    if(listviewSelectedUrl.contains("vocation")) {
                        surveyTypeInt = 1;
                    } else if (listviewSelectedUrl.contains("value")) {
                        surveyTypeInt = 2;
                    } else if (listviewSelectedUrl.contains("interest")) {
                        surveyTypeInt = 0;
                    } else {
                        surveyTypeInt = 3;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        tempBtn = (Button) findViewById(R.id.temp);
        tempBtn.setEnabled(false);
        tempBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PastResultActivity.this, SurveyResultActivity.class);
                intent.putExtra("type", Integer.toString(surveyTypeInt));
                intent.putExtra("userId", userId);
                intent.putExtra("userName", userName);
                intent.putExtra("userEmail", userEmail);
                intent.putExtra("userType", userType);
                intent.putExtra("userPk", userPk);

                switch (surveyTypeInt) {
                    case 1:
                        JobAptResultGetJson jobAptResultGetJson = new JobAptResultGetJson();
                        jobAptResultGetJson.execute(listviewSelectedUrl);
                        break;

                    case 2:
                        JobValueResultGetJson jobValueResultGetJson = new JobValueResultGetJson();
                        jobValueResultGetJson.execute(listviewSelectedUrl);
                        break;

                    case 3:
                        STEMResultGetJson stemResultGetJson = new STEMResultGetJson();
                        stemResultGetJson.execute(listviewSelectedUrl);
                        break;

                    case 0:
                        JobInteresetResultGetJson jobInteresetResultGetJson = new JobInteresetResultGetJson();
                        jobInteresetResultGetJson.execute(listviewSelectedUrl);

                    default:

                        break;
                }

            }
        });

        GetSurveyHistoryList getSurveyHistoryList = new GetSurveyHistoryList();
        getSurveyHistoryList.execute(requestHistoryUrl);

    }

    class GetSurveyHistoryList extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = progressDialog.show(PastResultActivity.this, "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            String result_string = result;

            try {
                JSONObject urlJsonObject = new JSONObject(result_string);
                String urlJsonObjectString = urlJsonObject.getString("answer");
                urlJsonArray = new JSONArray(urlJsonObjectString);
                for(int i=0;i<urlJsonArray.length();i++) {

                    if(urlJsonArray.getString(i).contains("vocation")) {
                        surveyType = "직업적성검사";
                    } else if (urlJsonArray.getString(i).contains("value")) {
                        surveyType = "직업가치관검사";
                    } else if (urlJsonArray.getString(i).contains("interest")) {
                        surveyType = "직업흥미검사";
                    } else {
                        surveyType = "이공계적합도검사";
                    }

                    adapter_pastResult.addItem(Integer.toString(i+1),surveyType,urlJsonArray.getString(i));

//                    GetSurveyResultList getSurveyResultList = new GetSurveyResultList();
//                    getSurveyResultList.execute(urlJsonArray.getString(i));
                }

                adapter_pastResult.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = (String) params[0];

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

//                GET 방식 사용할 경우, Body 붙여 보내면 error
//                OutputStream outputStream = httpURLConnection.getOutputStream();
//                outputStream.flush();
//                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
//                Log.d("###PastResultActivity", "GET response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();
            } catch (Exception e) {
                Log.d("###PastResultActivity", "doInBackGround error ", e);
                return new String("ERROR: " + e.getMessage());
            }

        }
    }

    class JobAptResultGetJson extends AsyncTask<String, Void, String> {
        private int statusCode;

        public String doInBackground(String... params) {
            String jobAptResultURL = params[0];
            Log.d("HS", "========================================================json함수 들어옴");

            try {
                // answers result 정보를 Json object로 만들어서
                JSONObject myJsonObject = new JSONObject();
                try {
                    //myJsonObject에 key : answers, value에 String 형태의 jobValueResult 추가
                    myJsonObject.put("url", jobAptResultURL);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // 서버 api에 전송을 시도한다
                URL obj = new URL("http://15.165.18.48/api/crawling/vocation");
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection(); // open connection

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
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

                        String resultString = jsonObject.get("result").toString();
                        Log.d("HS", "onPostExecute: RESULT STRING ======" + resultString);
                        resultString = resultString.substring(1, resultString.length()-1); // 앞뒤 { } 제거
                        String result2 = resultString;

                        String[] topAbilityArray =  resultString.split("\\],\"");
                        String[] topJobsArray = result2.split("\\],\"");

                        String[] mTopAbilityArray = topAbilityArray;
                        mTopAbilityArray[0] = topAbilityArray[0].split(":")[0].replace("\"","");
                        Log.d("HS", "onPostExecute: RESULT STRING ======" + mTopAbilityArray[0]);
                        String job1 = topJobsArray[0].split(":")[1].substring(1);
                        Log.d("HS", "onPostExecute: RESULT STRING 가공======" + topJobsArray[0].split(":")[1].substring(1));


                        topAbilityArray[1] = topAbilityArray[1].split(":")[0].replace("\"","");
                        Log.d("HS", "onPostExecute: RESULT STRING ======" + topAbilityArray[1]);
                        String job2 = topJobsArray[1].split(":")[1].substring(1);
                        Log.d("HS", "onPostExecute: RESULT STRING 가공======" + topJobsArray[1].split(":")[1].substring(1));

                        //topJobsArray[1] =  result2.split("\\],\"")[1].split(":")[1].replace("\"","");
                        //Log.d("HS", "JOB STRING ======" + topJobsArray[1]);

                        topAbilityArray[2] = topAbilityArray[2].split(":")[0].replace("\"","");
                        Log.d("HS", "onPostExecute: RESULT STRING ======" + topAbilityArray[2]);
                        String job3 = topJobsArray[2].split(":")[1].substring(1);
                        Log.d("HS", "onPostExecute: RESULT STRING 가공======" + topJobsArray[2].split(":")[1].substring(1));

                        String finalAbilityString = mTopAbilityArray[0] + " / " + mTopAbilityArray[1] + " / " + mTopAbilityArray[2];
                        String finalJobString = job1 + "," + job2 + "," + job3;
                        finalJobString = finalJobString.substring(0,finalJobString.length()-1).replace("\"","");
                        finalJobString = finalJobString.replace(",",", ");
                        Log.d("HS", "onPostExecute: FINAL===> " + finalAbilityString);
                        Log.d("HS", "onPostExecute: FINAL===> " + finalJobString);

                        Intent intent = new Intent(PastResultActivity.this, SurveyResultActivity.class);
                        intent.putExtra("topAbility", finalAbilityString);
                        intent.putExtra("topJobs", finalJobString);
                        intent.putExtra("type", "1");
                        intent.putExtra("userId", userId);
                        intent.putExtra("userName", userName);
                        intent.putExtra("userEmail", userEmail);
                        intent.putExtra("userType", userType);
                        intent.putExtra("userPk", userPk);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    Log.d("json1", e.getMessage());
                }
            }
        }
    }

    class JobInteresetResultGetJson extends AsyncTask<String, Void, String> {
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

                        Intent intent = new Intent(PastResultActivity.this, SurveyResultActivity.class);
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

    class JobValueResultGetJson extends AsyncTask<String, Void, String> {
        private int statusCode;

        public String doInBackground(String... params) {
            String jobValueResultURL = params[0];
            Log.d("HS", "========================================================json함수 들어옴");

            try {
                // answers result 정보를 Json object로 만들어서
                JSONObject myJsonObject = new JSONObject();
                try {
                    //myJsonObject에 key : answers, value에 String 형태의 jobValueResult 추가
                    myJsonObject.put("url", jobValueResultURL);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // 서버 api에 전송을 시도한다
                URL obj = new URL("http://15.165.18.48/api/crawling/value");

                HttpURLConnection conn = (HttpURLConnection) obj.openConnection(); // open connection

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
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
                Toast.makeText(getApplicationContext(), "정보가 잘못되었습니다.", Toast.LENGTH_SHORT).show();

            } else {
                try {
                    // 수신한 data s에 대해
                    JSONObject jsonObject = new JSONObject(s);
                    if (statusCode == HttpURLConnection.HTTP_OK || statusCode == HttpURLConnection.HTTP_CREATED) {
                        // 데이터들을 추출하여 변수에 저장한다.

                        JSONObject resultObject= jsonObject.getJSONObject("result");
                        String topTwoVal = resultObject.getString("value");

                        String finaltopTwoVal = topTwoVal.substring(1, topTwoVal.length()-1);
                        finaltopTwoVal = finaltopTwoVal.replace("\"","").replace(",",", ");

                        String suitableJobs = resultObject.getString("jobs");
                        String finalsuitableJobs = suitableJobs.substring(9);
                        finalsuitableJobs = finalsuitableJobs.split("]")[0].replace("\"","");;

                        String[] topSuitableJobs =finalsuitableJobs.split(",");
                        String topFinalSuitableJob = new String();
                        for(int i=0;i<20;i++){
                            if(i == 19){
                                topFinalSuitableJob = topFinalSuitableJob + topSuitableJobs[i];
                                break;
                            }
                            topFinalSuitableJob = topFinalSuitableJob + topSuitableJobs[i] +", ";
                        }

                        Intent intent = new Intent(PastResultActivity.this, SurveyResultActivity.class);
                        intent.putExtra("topTwoVal", finaltopTwoVal);
                        intent.putExtra("suitableJobs", topFinalSuitableJob);
                        intent.putExtra("type", "2");
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

    class STEMResultGetJson extends AsyncTask<String, Void, String> {
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
                URL obj = new URL("http://15.165.18.48/api/crawling/engineering");

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

                        String verySuitable = resultObject.getString("매우 적합");
                        String suitable = resultObject.getString("적합");
                        Log.d("json1", "========================================================변수저장");
                        Log.d("json1", verySuitable);
                        Log.d("json1", suitable);

                        Intent intent = new Intent(PastResultActivity.this, SurveyResultActivity.class);
                        intent.putExtra("verySuitable", verySuitable);
                        intent.putExtra("suitable", suitable);
                        intent.putExtra("type", "3");
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