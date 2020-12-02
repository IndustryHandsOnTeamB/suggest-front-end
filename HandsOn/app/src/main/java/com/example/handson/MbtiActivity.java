package com.example.handson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.util.Linkify;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MbtiActivity extends AppCompatActivity {

    int myUserPk;

    Button eButton, iButton, nButton, sButton, tButton, fButton, jButton, pButton;
    boolean iBtnPressed = false, nBtnPressed = false, tBtnPressed = false, jBtnPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_set_mbti);

        Intent intent = getIntent();
        myUserPk = intent.getIntExtra("userPk", 4444);
        Log.d("intent","=================================Mbti에서 받은 userPk: "+myUserPk);

        eButton = (Button)findViewById(R.id.e_button);
        iButton = (Button)findViewById(R.id.i_button);
        nButton = (Button)findViewById(R.id.n_button);
        sButton = (Button)findViewById(R.id.s_button);
        tButton = (Button)findViewById(R.id.t_button);
        fButton = (Button)findViewById(R.id.f_button);
        jButton = (Button)findViewById(R.id.j_button);
        pButton = (Button)findViewById(R.id.p_button);

        TextView mbtiLink = (TextView)findViewById(R.id.mbti_link);
        String mbtiTestText = mbtiLink.getText().toString();
        Linkify.TransformFilter mTransform = new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                return "";
            }
        };
        Pattern pattern = Pattern.compile(mbtiTestText);
        Linkify.addLinks(mbtiLink, pattern, "https://www.16personalities.com/ko/%EB%AC%B4%EB%A3%8C-%EC%84%B1%EA%B2%A9-%EC%9C%A0%ED%98%95-%EA%B2%80%EC%82%AC", null, mTransform);

        Button okButton = (Button)findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myMbti = getMbtiFromButtons();
                Log.d("Tag","내 mbti: "+myMbti);

                MbtiJson signIn = new MbtiJson();
                signIn.execute(myMbti);
                finish();

            }
        });

        iButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonColor(iButton,eButton);
                iBtnPressed = true;
            }
        });
        eButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonColor(eButton,iButton);
                iBtnPressed = false;
            }
        });

        nButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonColor(nButton,sButton);
                nBtnPressed = true;
            }
        });
        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               setButtonColor(sButton,nButton);
                nBtnPressed = false;
            }
        });

        tButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonColor(tButton,fButton);
                tBtnPressed = true;
            }
        });
        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonColor(fButton,tButton);
                tBtnPressed = false;
            }
        });

        jButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonColor(jButton, pButton);
                jBtnPressed = true;
            }
        });
        pButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonColor(pButton, jButton);
                jBtnPressed = false;
            }
        });
    }

    void setButtonColor(Button btn1, Button btn2){
        btn1.setBackground(getResources().getDrawable(R.drawable.button_lightblue));
        btn2.setBackground(getResources().getDrawable(R.drawable.button_lightgray));
    }

    String getMbtiFromButtons(){
        String myMbti = "";
        if(iBtnPressed) myMbti=myMbti.concat("I");
        else  myMbti=myMbti.concat("E");
        if(nBtnPressed) myMbti=myMbti.concat("N");
        else  myMbti=myMbti.concat("S");
        if(tBtnPressed) myMbti=myMbti.concat("T");
        else  myMbti=myMbti.concat("F");
        if(jBtnPressed) myMbti=myMbti.concat("J");
        else  myMbti=myMbti.concat("P");

        return myMbti;
    }

    public class MbtiJson extends AsyncTask<String, Void, String> {
        private int statusCode;
        public String doInBackground(String... params) {
            String mbti = params[0];

            try {
                //입력한 정보를 Json object로 만들어서
                JSONObject myJsonObject = new JSONObject();
                try {
                    myJsonObject.put("mbti", mbti);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // 서버 api에 전송을 시도한다
                URL obj = new URL("http://15.165.18.48/api/v1/users/"
                        + String.valueOf(myUserPk) + "/mbti/save/");

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
                Log.d("mbti","-----------------------null from server---------------------");
            } else {
                try {
                    // 수신한 data s에 대해
                    JSONObject jsonObject = new JSONObject(s);

                    if(statusCode == 201){
                        // 데이터들을 추출하여 변수에 저장한다.
                        //myId = jsonObject.get("username").toString();
                        //myName = jsonObject.get("name").toString();
                        //myEmail = jsonObject.get("email").toString();
                        //int tempType = Integer.parseInt(jsonObject.get("user_type").toString());
                        Log.d("mbti","-----------------------mbtitype 등록---------------------");
                        //Toast.makeText(getApplicationContext(), "내 MBTI 등록", Toast.LENGTH_SHORT).show();


                    }

                } catch (JSONException e) {

                }
            }
        }

    }
}
