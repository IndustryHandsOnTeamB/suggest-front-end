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
import android.widget.TextView;
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
    private TextView contentsText;
    private String surveyType, userType, requestURL;
    private String userId, userName, userEmail;
    private int userPk;

    TextView textview;

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
                switch (position){
                    case 0:
                        String testContents1 ="문항별로 평소 생각이나 태도에 따라 ‘(나는) 매우 그렇다’ 싶으면 7점으로, ‘(나와) 전혀 다르다’면 1점으로 응답해주세요. 문항마다‘학생 자신에게 맞는’ 점수를 번호에 표시해주세요.\n" +
                                "\n" +
                                "검사 진행방법\n" +
                                "예를 들어 '글을 잘 이해한다.' 의 문항이 있습니다.\n" +
                                "만약 자신의 글 이해력이 매우 낮다고 생각할 때는 1점을, 매우 높다고 생각될 때는 7점에 답을 할 수 있습니다.\n" +
                                "간단한 글은 조금 이해하지만 국어책의 내용이 잘 이해가 안 된다면 낮은 쪽부터 높은 쪽으로 2,3,4점 중의 하나에 답을 할 수 있습니다.";
                        contentsText.setText(testContents1);
                        break;
                    case 1:
                        String testContents2 ="직업과 관련된 두 개 가치 중에서 자기에게 더 중요한 가치에 표시하세요.\n" +
                                "\n" +
                                "검사 진행방법\n" +
                                "만약 ‘능력발휘’ 보다 ‘자율성’ 이 더 중요하다면 ‘자율성’ 을 체크하세요.\n" +
                                "반대로, ‘능력발휘’ 가 ‘ 자율성’ 보다 중요하다면 ‘능력발휘’ 에 체크하세요.";
                        contentsText.setText(testContents2);
                        break;
                    case 2:
                        String testContents3 ="검사지에는 문항을 읽고 다음 활동들을 얼마나 좋아하는지를 생각해보고 답하십시오.\n" +
                                "\n" +
                                "검사 진행방법\n" +
                                "‘제과점에서 빵, 과자 만든다.’라는 예시문항에서\n" +
                                "만약 여러분이 ‘제과점에서 빵, 과자를 만드는 것을 생각해보고\n" +
                                "해당 행위가 매우 싫다면 ‘매우 싫다’, 약간 싫다면 ‘약간싫다’,\n" +
                                "또는 빵, 과자를 만드는 행위가 좋다면 ‘약간좋다’, ‘매우좋다’로 응답할 수 있습니다.";
                        contentsText.setText(testContents3);
                        break;
                    case 3:
                        String testContents4 ="검사지에는 문항과 일치정도가 있습니다. 문항은 자신의 생각이나 행동에 대한 예시입니다.\n" +
                                "\n" +
                                "검사 진행방법\n" +
                                "‘건축설계 – 개념구축 및 공간화 방법, 설계도 작성 및 모형 제작’ 라는 예시문항에서\n" +
                                "제시된 교과를 공부하기 어려울 것 같고, 선호하지 않는다면, ‘못할것같다’ 또는 ‘전혀 못할것같다’ 를\n" +
                                "잘 공부할 수 있고 좋아하는 교과라고 생각되면, ‘잘할것같다’ 또는 ‘매우 잘할것같다’ 에 답을 할 수 있습니다.";
                        contentsText.setText(testContents4);
                        break;
                }
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
                userType_spinner.setSelection(0);
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
      
//        textview = (TextView)findViewById(R.id.textview_startnewsurvey_info);
        contentsText = findViewById(R.id.text_test_contents);

        startBtn = (Button)findViewById(R.id.button_startnewsurvey_start);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetSurvey getSurvey = new GetSurvey();

                switch(surveyType) {
                    case "직업적성검사":
                        if(userType.contentEquals("고등학생")) {
                            requestURL = "http://15.165.18.48/api/v1/users/"+userPk +"/question/high/aptitude";
//                            textview.setText("적성은 지금 현재 내가 잘하고 있거나 앞으로 발전할 가능성이 높은 능력을 뜻합니다.");
                            getSurvey.execute(requestURL);
                        } else if(userType.contentEquals("중학생")) {
                            requestURL = "http://15.165.18.48/api/v1/users/"+userPk +"/question/middle/aptitude";
//                            textview.setText("적성은 지금 현재 내가 잘하고 있거나 앞으로 발전할 가능성이 높은 능력을 뜻합니다.");
                            getSurvey.execute(requestURL);
                        } else {
                            requestURL = "http://";
                            Toast.makeText(getApplicationContext(),"해당 검사는 중,고등학생용입니다.",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "직업가치관검사":
                        requestURL = "http://15.165.18.48/api/v1/users/"+userPk+"/question/common/value";
//                        textview.setText("직업가치관검사는 여러분이 직업을 선택할 때 상대적으로 어떠한 가치를 중요하게 생각하는지를 알려줍니다. ");
                        getSurvey.execute(requestURL);
                        break;
                    case "직업흥미검사":
                        if(userType.contentEquals("고등학생")) {
                            requestURL = "http://15.165.18.48/api/v1/users/"+userPk+"/question/high/interest";
//                            textview.setText("직업흥미검사는 중·고등학생들이 직업과 관련하여 자신의 흥미를 파악하고, 다양한 직업들 중에서 자신에게 적합한 직업을 탐색하는 데 도움을 주기 위해 개발된 검사입니다.");
                            getSurvey.execute(requestURL);
                        } else if(userType.contentEquals("중학생")) {
                            requestURL = "http://15.165.18.48/api/v1/users/"+userPk+"/question/middle/interest";
//                            textview.setText("직업흥미검사는 중·고등학생들이 직업과 관련하여 자신의 흥미를 파악하고, 다양한 직업들 중에서 자신에게 적합한 직업을 탐색하는 데 도움을 주기 위해 개발된 검사입니다.");
                            getSurvey.execute(requestURL);
                        } else {
                            requestURL = "http://";
//                            textview.setText("이공계전공적합도검사는 이공계 내의 전공을 선택하고자 할 때, 전공군별 상대적 적합도를 평가해 볼 수 있도록 도와 주기 위하여 개발된 검사 입니다.");
                            Toast.makeText(getApplicationContext(),"해당 검사는 중,고등학생용입니다.",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "이공계가치관검사":
                        if(userType.contentEquals("일반인")){
                            requestURL = "http://15.165.18.48/api/v1/users/"+userPk+"/question/common/se";
                            getSurvey.execute(requestURL);
                        } else{
                            requestURL = "http://";
                            Toast.makeText(getApplicationContext(),"해당 검사는 일반용입니다.",Toast.LENGTH_SHORT).show();
                        }
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
}