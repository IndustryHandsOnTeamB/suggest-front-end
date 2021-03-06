package com.example.handson;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class StartNewSurveyActivity extends AppCompatActivity {

    private Spinner surveyType_spinner, userType_spinner;
    private Button startBtn, cancelBtn;
    private String surveyType, userType, requestURL;
    private String userId, userName, userEmail;
    private int userPk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_new_survey);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        userName = intent.getStringExtra("userName");
        userEmail = intent.getStringExtra("userEmail");
        userType = intent.getStringExtra("userType");
        userPk = intent.getIntExtra("userPk",4444);

        surveyType_spinner = (Spinner)findViewById(R.id.spinner_startnewsurvey_testtype);
        surveyType_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?>  parent, View view, int position, long id) {
                surveyType = surveyType_spinner.getSelectedItem().toString();
            }
            public void onNothingSelected(AdapterView<?>  parent) {
            }
        });

        userType_spinner = (Spinner)findViewById(R.id.spinner_startnewsurvey_usertype);
        switch (userType) {
            case "중학생":
                userType_spinner.setSelection(0);
                break;
            case "고등학생":
                userType_spinner.setSelection(1);
                break;
            case "일반":
                userType_spinner.setSelection(2);
                break;
            default:

                break;
        }
        userType_spinner.setEnabled(false);
        userType_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?>  parent, View view, int position, long id) {
                userType = userType_spinner.getSelectedItem().toString();
            }
            public void onNothingSelected(AdapterView<?>  parent) {
            }
        });

        startBtn = (Button)findViewById(R.id.button_startnewsurvey_start);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetSurvey getSurvey = new GetSurvey();

                switch(surveyType) {
                    case "직업적성검사":
                        if(userType.contentEquals("고등학생")) {
                            requestURL = "http://15.165.18.48/api/v1/users/"+userPk +"/question/high/aptitude";
                            getSurvey.execute(requestURL);
                        } else if(userType.contentEquals("중학생")) {
                            requestURL = "http://15.165.18.48/api/v1/users/"+userPk +"/question/middle/aptitude";
                            getSurvey.execute(requestURL);
                        } else {
                            requestURL = "http://";
                            Toast.makeText(getApplicationContext(),"해당 검사는 중,고등학생용입니다.",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "직업가치관검사":
                        requestURL = "http://15.165.18.48/api/v1/users/"+userPk+"/question/common/value";
                        getSurvey.execute(requestURL);
                        break;
                    case "직업흥미검사":
                        if(userType.contentEquals("고등학생")) {
                            requestURL = "http://15.165.18.48/api/v1/users/"+userPk+"/question/high/interest";
                            getSurvey.execute(requestURL);
                        } else if(userType.contentEquals("중학생")) {
                            requestURL = "http://15.165.18.48/api/v1/users/"+userPk+"/question/middle/interest";
                            getSurvey.execute(requestURL);
                        } else {
                            requestURL = "http://";
                            Toast.makeText(getApplicationContext(),"해당 검사는 중,고등학생용입니다.",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "이공계가치관검사":
                        requestURL = "http://15.165.18.48/api/v1/users/"+userPk+"/question/common/se";
                        getSurvey.execute(requestURL);
                        break;
                    default:
                        Toast.makeText(getApplicationContext(),"처리 과정에서 오류가 발생했습니다.",Toast.LENGTH_SHORT).show();
                        break;
                }

                Log.d("###requestURL",requestURL);
            }
        });

        cancelBtn = (Button)findViewById(R.id.button_startnewsurvey_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    class GetSurvey extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = progressDialog.show(StartNewSurveyActivity.this, "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            String result_string = result;

            try {
                JSONObject jsonObject = new JSONObject(result_string);
                String jsonSuccess = jsonObject.getString("SUCC_YN");
                String jsonResult = jsonObject.getString("RESULT");

                Log.d("###StartNewSurveyActivity - jsonResult",jsonResult);

                if (jsonSuccess.contentEquals("Y")) {
                    switch(surveyType) {
                        case "직업적성검사":
                            Intent intent = new Intent(StartNewSurveyActivity.this, SurveyJobAptitudeActivity.class);
                            intent.putExtra("userId", userId);
                            intent.putExtra("userName", userName);
                            intent.putExtra("userEmail", userEmail);
                            intent.putExtra("userType", userType);
                            intent.putExtra("userPk", userPk);
                            intent.putExtra("jsonResult", jsonResult);
                            startActivity(intent);
                            break;
                        case "직업가치관검사":
                            Intent intent2 = new Intent(StartNewSurveyActivity.this, SurveyJobValueActivity.class);
                            intent2.putExtra("userId", userId);
                            intent2.putExtra("userName", userName);
                            intent2.putExtra("userEmail", userEmail);
                            intent2.putExtra("userType", userType);
                            intent2.putExtra("userPk", userPk);
                            intent2.putExtra("jsonResult", jsonResult);
                            startActivity(intent2);
                            break;
                        case "직업흥미검사":
                            Intent intent3 = new Intent(StartNewSurveyActivity.this, SurveyJobInterestActivity.class);
                            intent3.putExtra("userId", userId);
                            intent3.putExtra("userName", userName);
                            intent3.putExtra("userEmail", userEmail);
                            intent3.putExtra("userType", userType);
                            intent3.putExtra("userPk", userPk);
                            intent3.putExtra("jsonResult", jsonResult);
                            startActivity(intent3);
                            break;
                        case "이공계가치관검사":
                            Intent intent4 = new Intent(StartNewSurveyActivity.this, SurveySTEMMajorSuitabilityActivity.class);
                            intent4.putExtra("userId", userId);
                            intent4.putExtra("userName", userName);
                            intent4.putExtra("userEmail", userEmail);
                            intent4.putExtra("userType", userType);
                            intent4.putExtra("userPk", userPk);
                            intent4.putExtra("jsonResult", jsonResult);
                            startActivity(intent4);
                            break;
                        default:
                            Toast.makeText(getApplicationContext(),"처리 과정에서 오류가 발생했습니다.",Toast.LENGTH_SHORT).show();
                            break;
                    }

                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "서버 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("###StartNewSurveyActivity",result_string);
        }

        @Override
        protected String doInBackground(String...params) {

            String serverURL = (String)params[0];

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
                Log.d("###StartNewSurveyActivity", "GET response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else {
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
                Log.d("###StartNewSurveyActivity", "doInBackGround error ", e);
                return new String("ERROR: " + e.getMessage());
            }
        }
    }

    class LoginTest extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = progressDialog.show(StartNewSurveyActivity.this, "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            String result_string = result;

            Log.d("###StartNewSurveyActivity",result_string);
        }

        @Override
        protected String doInBackground(String...params) {
//            String input1 = (String)params[1];
//            String input2 = (String)params[2];

            String serverURL = (String)params[0];
//            String postParameters = "input1=" + input1; // + "&input2=" + input2;

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
                httpURLConnection.connect();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("username", "test1");
//                jsonObject.put("email", "test@gamil.com");
//                jsonObject.put("name", "test1");
                jsonObject.put("input_password", "test1");

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(jsonObject.toString().getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("###StartNewSurveyActivity", "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else {
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
                Log.d("###StartNewSurveyActivity", "doInBackGround error ", e);
                return new String("ERROR: " + e.getMessage());
            }
        }
    }
}