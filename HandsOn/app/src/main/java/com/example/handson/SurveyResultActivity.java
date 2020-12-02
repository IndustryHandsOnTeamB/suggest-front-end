package com.example.handson;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SurveyResultActivity extends AppCompatActivity {

    Button moveMainMenuButton;
    TextView resultText1;
    TextView resultText2;

    ExpandableListView mbtiListView;
    MbtiListViewAdapter mbtiListViewAdapter;
    String mbtiRecommendText = "";
    String mbtiTitleText = "";

    /*
    설문 진행 후 결과창으로 넘어올 때 설문 타입을 넘겨받음
    0 : 직업 흥미 검사
    1 : 직업 적성 검사
    2 : 직업 가치관 검사
    3 : 이공계 적합도 검사
    */
    int surveyType = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_result);

        moveMainMenuButton = findViewById(R.id.button_main_menu);
        mbtiListView = (ExpandableListView) findViewById(R.id.expandable_list_view_mbti_job);

        LinearLayout surveyResultLayout = (LinearLayout)findViewById(R.id.layout_survey_result_dynamic);
        ResultItem resultItem;

        /*
        Intent getIntent = getIntent();
        surveyType = intent.getIntExtra("surveyType", 4444);
        */

        switch (surveyType){
            case 0:
                resultItem = new ResultJobInterestItem(this);
                resultText1 = findViewById(R.id.text_job_interest_result1);
                resultText2 = findViewById(R.id.text_job_interest_result2);
                break;
            case 1:
                resultItem = new ResultJobAptitudeItem(this);
                resultText1 = findViewById(R.id.text_job_apt_result1);
                resultText2 = findViewById(R.id.text_job_apt_result2);
                break;
            case 2:
                resultItem = new ResultJobValueItem(this);
                resultText1 = findViewById(R.id.text_job_value_result1);
                resultText2 = findViewById(R.id.text_job_value_result2);
                break;
            case 3:
                resultItem = new ResultSTEMItem(this);
                resultText1 = findViewById(R.id.text_stem_result1);
                resultText2 = findViewById(R.id.text_stem_result2);
                break;
            default:
                resultItem = new ResultItem(this);
        }

        surveyResultLayout.addView(resultItem);

        //결과 url에서 파싱한 결과 설정
        //resultText1.setText("");
        //resultText2.setText("");

        MbtiListGetJSON mbtiJson = new MbtiListGetJSON();
        mbtiJson.execute(String.valueOf(MenuSelect.userPk));

        mbtiTitleText = "나의 MBTI 추천 직업은?";

        mbtiListViewAdapter = new MbtiListViewAdapter(getApplicationContext(), R.layout.mbti_title_item, R.layout.mbti_job_list_item, mbtiTitleText, mbtiRecommendText);
        mbtiListView.setAdapter(mbtiListViewAdapter);

        moveMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SurveyResultActivity.this, MenuSelect.class);
                startActivity(intent);
            }
        });
    }

    public void updateMbtiListView(){
        mbtiListViewAdapter = new MbtiListViewAdapter(getApplicationContext(), R.layout.mbti_title_item, R.layout.mbti_job_list_item, mbtiTitleText, mbtiRecommendText);
        mbtiListView.setAdapter(mbtiListViewAdapter);
    }

    public class MbtiListGetJSON extends AsyncTask<String, Void, String> {
        private int statusCode;

        public String doInBackground(String... params) {
            String userPk = params[0];

            try {
                // 서버 api에 전송을 시도한다
                String url = "http://15.165.18.48/api/v1/users/"+userPk+"/mbti/view";
                URL obj = new URL(url);

                HttpURLConnection conn = (HttpURLConnection) obj.openConnection(); // open connection

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                // 데이터를 읽어올 것이고
                conn.setDoInput(true);

                // property 지정해주고
                conn.setRequestProperty("accept", "application/json");

                statusCode = conn.getResponseCode();

                if (statusCode == HttpURLConnection.HTTP_OK || statusCode == HttpURLConnection.HTTP_CREATED) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    JSONObject result = new JSONObject(response.toString());
                    mbtiTitleText = result.getString("mbti")+"의 추천 직업은?";
                    mbtiRecommendText = result.getString("job");
                    updateMbtiListView();
                } else {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                }
            } catch (Exception e) {
                Log.e("json", "ConnectionException");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
