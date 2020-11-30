package com.example.handson;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PastResultActivity extends AppCompatActivity {

    public static ListView listview_pastResult;
    Button tempBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_result);

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
               Intent tempIntent = new Intent(PastResultActivity.this, StartNewSurveyActivity.class);
               startActivity(tempIntent);
            }
        });

    }

    // Class for getting past results
    class GetPastResult extends AsyncTask<String, Void, String> {
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

            Log.d("###PastResultActivity",result_string);
        }

        @Override
        protected String doInBackground(String...params) {
            String input1 = (String)params[1];
            String input2 = (String)params[2];

            String serverURL = (String)params[0];
            String postParameters = "input1=" + input1 + "&input2=" + input2;

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("###PastResultActivity", "POST response code - " + responseStatusCode);

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
                Log.d("###PastResultActivity", "doInBackGround error ", e);
                return new String("ERROR: " + e.getMessage());
            }
        }
    }
}