package com.example.handson;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PastResultActivity extends AppCompatActivity {

    public static ListView listview_pastResult;
    ArrayList<PastResult_listview> arrayList = new ArrayList<>();
    PastResult_listviewadapter adapter_pastResult = new PastResult_listviewadapter(arrayList, this);

    Button tempBtn;

    private String userId, userName, userEmail, userType, surveyType, requestHistoryUrl, requestResultURL;
    private int userPk;

    private String listviewSelectedUrl="";

    JSONArray urlJsonArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_result);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        userName = intent.getStringExtra("userName");
        userEmail = intent.getStringExtra("userEmail");
        userType = intent.getStringExtra("userType");
        userPk = intent.getIntExtra("userPk", 4444);

        requestHistoryUrl = "http://15.165.18.48/api/v1/users/" + 3 + "/testhistory";

        listview_pastResult = (ListView) findViewById(R.id.listview_pastresult);
        listview_pastResult.setAdapter(adapter_pastResult);

        listview_pastResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    listviewSelectedUrl = urlJsonArray.get(position).toString();
                    tempBtn.setEnabled(true);
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
//                Intent intent = new Intent(PastResultActivity.this, StartNewSurveyActivity.class);
//                intent.putExtra("userId", userId);
//                intent.putExtra("userName", userName);
//                intent.putExtra("userEmail", userEmail);
//                intent.putExtra("userType", userType);
//                intent.putExtra("userPk", userPk);
//                startActivity(intent);

                GetSurveyResultList getSurveyResultList = new GetSurveyResultList();
                getSurveyResultList.execute(listviewSelectedUrl);
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

    class GetSurveyResultList extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String result_string = result;

            try {
                JSONObject resultJsonObject = new JSONObject(result_string);
                String resultJsonString = resultJsonObject.getString("result");

                Log.d("###resultFromServer", resultJsonString);

                //받은 JSON parsing
                
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String historyURL = (String) params[0];
            Log.d("###historyUrl", historyURL);
            String serverURL = "";

            if(historyURL.contains("vocation")) {
                serverURL = "http://15.165.18.48/api/crawling/vocation";
            } else if (historyURL.contains("value")) {
                serverURL = "http://15.165.18.48/api/crawling/value";
            } else if (historyURL.contains("interest")) {
                serverURL = "http://15.165.18.48/api/crawling/interest";
            } else {
                serverURL = "http://15.165.18.48/api/crawling/engineering";
            }

            Log.d("###serverUrl", serverURL);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("url", historyURL);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("###jsonObject", jsonObject.toString());

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-type", "application/json");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(jsonObject.toString().getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("###PastResultActivity", "POST response code - " + responseStatusCode);

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
}