package com.example.handson;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PastResultActivity extends AppCompatActivity {

    public static ListView listview_pastResult;
    Button tempBtn;

    private String userId,userName,userEmail,userType, requestURL;
    private int userPk;
    private boolean isLoaded = false;

    WebView webview;
    public String html;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_result);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        userName = intent.getStringExtra("userName");
        userEmail = intent.getStringExtra("userEmail");
        userType = intent.getStringExtra("userType");
        userPk = intent.getIntExtra("userPk",4444);

        requestURL = "http://15.165.18.48/api/v1/users/" + userPk + "/testhistory";

        webview = (WebView) findViewById(R.id.webview_pastresult);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new MyJavascriptInterface(), "Android");
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('body')[0].innerHTML);");
            }
        });

        webview.loadUrl("https://inspct.career.go.kr/web/psycho/engineering/report?seq=NTI1NTc5NjQ");

        ArrayList<PastResult_listview> arrayList = new ArrayList<>();
        arrayList.add(0, new PastResult_listview("time","date","result"));

        listview_pastResult = (ListView)findViewById(R.id.listview_pastresult);
        PastResult_listviewadapter adpter_pastResult = new PastResult_listviewadapter(arrayList,this);
        adpter_pastResult.addItem("a", "a","a");
        listview_pastResult.setAdapter(adpter_pastResult);

        tempBtn = (Button)findViewById(R.id.temp);
        tempBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PastResultActivity.this, StartNewSurveyActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("userName", userName);
                intent.putExtra("userEmail", userEmail);
                intent.putExtra("userType", userType);
                intent.putExtra("userPk", userPk);
                startActivity(intent);
            }
        });

        final Document[] document = {null};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    document[0] = Jsoup.connect("https://inspct.career.go.kr/web/psycho/engineering/report?seq=NTI1NTc5NjQ").userAgent("Chrome").get();
                    String tempString = document[0].select("div.cont_result p").text();
                    Document webviewhtml = Jsoup.parse(html);
                    String temp2String = webviewhtml.select("ft_type2").text();
                    Log.d("###PastResultActivity",temp2String);
                    String dateString = document[0].select("tr.ft_type2").text();
//                    Log.d("###PastResultActivity",dateString);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



//        while(true) {
//            Log.d("###PastResultActivity","While");
//            if(isLoaded) {
//                thread.start();
//                break;
//            }
//        }
    }

    public class MyJavascriptInterface {
        @JavascriptInterface
        public void getHtml (String result) {
            html = result;
            isLoaded = true;
            Log.d("###url",html);

//            Log.d("###PastResultActivity",html);
        }
    }

    class GetSurveyHistory extends AsyncTask<String, Void, String> {
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
                JSONObject jsonObject = new JSONObject(result_string);
                String jsonSuccess = jsonObject.getString("SUCC_YN");

                if (jsonSuccess.contentEquals("Y")) {

                } else {
                    Toast.makeText(getApplicationContext(), "서버 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            Log.d("###PastResultActivity",result_string);
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
//                Log.d("###PastResultActivity", "GET response code - " + responseStatusCode);

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
//                Log.d("###PastResultActivity", "doInBackGround error ", e);
                return new String("ERROR: " + e.getMessage());
            }
        }
    }
}