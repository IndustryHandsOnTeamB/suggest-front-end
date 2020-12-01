package com.example.handson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LogInActivity extends AppCompatActivity {

    String myId, myName, myEmail, myType;
    public static int myUserPk;
    boolean logInSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = (Button)findViewById(R.id.login_button);
        Button signUpButton = (Button)findViewById(R.id.signup_button);

        final EditText userId = (EditText)findViewById(R.id.user_id);
        final EditText userPwd = (EditText)findViewById(R.id.user_pwd);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = userId.getText().toString();
                String pwd = userPwd.getText().toString();

                SignInJson signIn = new SignInJson();
                signIn.execute(id, pwd);

                /*
                if(logInSuccess) {
                    Intent intent = new Intent(getApplicationContext(), MenuSelect.class);
                    intent.putExtra("userId", myId);
                    intent.putExtra("userName", myName);
                    intent.putExtra("userEmail", myEmail);
                    intent.putExtra("userType", myType);
                    intent.putExtra("userPk", myUserPk);

                    finish();
                    startActivity(intent);
                }
                else{

                    Log.d("json","=======================================================loginsuccess false");
                    //로그인 실패
                    Toast.makeText(getApplicationContext(),"로그인 정보가 잘못되었습니다.",Toast.LENGTH_SHORT).show();
                }
                */

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    public int getMyUserPk(){
        return myUserPk;
    }

    //json object send & receive
    public class SignInJson extends AsyncTask<String, Void, String> {
        private int statusCode;
        public String doInBackground(String... params) {
            String id = params[0];
            String pw = params[1];
            Log.d("json","========================================================json함수 들어옴");

            try {
                // 로그인 시 입력한 정보를 Json object로 만들어서
                JSONObject myJsonObject = new JSONObject();
                try {
                    myJsonObject.put("username", id);
                    myJsonObject.put("input_password", pw);
                    myJsonObject.put("email", "email");
                    myJsonObject.put("name", "name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                // 서버 api에 전송을 시도한다
                URL obj = new URL("http://15.165.18.48/api/v1/users/signin/");

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
                Log.d("json","========================================================null");
                Toast.makeText(getApplicationContext(),"로그인 정보가 잘못되었습니다.",Toast.LENGTH_SHORT).show();

            } else {
                try {
                    // 수신한 data s에 대해
                    JSONObject jsonObject = new JSONObject(s);

                    if(statusCode == 200){
                        Log.d("json","========================================================200");
                        logInSuccess = true;
                        Log.d("json","========================================================loginSuccess true");

                        // 데이터들을 추출하여 변수에 저장한다.
                        myUserPk = Integer.parseInt(jsonObject.get("id").toString());
                        myId = jsonObject.get("username").toString();
                        myName = jsonObject.get("name").toString();
                        myEmail = jsonObject.get("email").toString();
                        myType = jsonObject.get("user_type").toString();
                        Log.d("json","========================================================변수저장");

                        Intent intent = new Intent(getApplicationContext(), MenuSelect.class);
                        intent.putExtra("userId", myId);
                        intent.putExtra("userName", myName);
                        intent.putExtra("userEmail", myEmail);
                        intent.putExtra("userType", myType);
                        intent.putExtra("userPk", myUserPk);

                        finish();
                        startActivity(intent);

                    }

                } catch (JSONException e) {

                }
            }
        }

    }
}


