package com.example.handson;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final boolean usableId = true;
        final String[] userTypeList = {"중학생","고등학생","대학생","기타"};

        final Spinner userTypeSpinner = (Spinner)findViewById(R.id.user_type_spinner);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, userTypeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSpinner.setAdapter(adapter);

        //Button idCheckButton = (Button)findViewById(R.id.id_check_button);
        Button signUpButton = (Button)findViewById(R.id.join_button);
        Button cancelButton = (Button)findViewById(R.id.cancel_button);

        final EditText userName = (EditText)findViewById(R.id.user_name);
        final EditText userEmail = (EditText)findViewById(R.id.email);
        final EditText userId = (EditText)findViewById(R.id.user_id);
        final EditText userPwd = (EditText)findViewById(R.id.user_pwd);
        final EditText userPwdCheck = (EditText)findViewById(R.id.user_pwd_check);


/*
        idCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String newId = userId.getText().toString();
                ////같은 아이디 있는지 check
                //if(usableId)
                //    Toast.makeText(getApplicationContext(),"사용가능한 아이디입니다.",Toast.LENGTH_SHORT).show();
                //else
                //    Toast.makeText(getApplicationContext(),"이미 사용중인 아이디입니다.",Toast.LENGTH_SHORT).show();

            }
        });
*/
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPwd = userPwd.getText().toString();
                String newPwdCheck = userPwdCheck.getText().toString();
                boolean pwdIsSame = false;
                if(newPwd.equals(newPwdCheck))
                    pwdIsSame = true;

                if(!usableId)
                    Toast.makeText(getApplicationContext(),"아이디를 확인해주세요.",Toast.LENGTH_SHORT).show();
                else if(!pwdIsSame)
                    Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                else{
                    final String[] userInfo = new String[4];    //id, password, name, email. user type
                    userInfo[0] = userId.getText().toString();
                    userInfo[1] = userPwd.getText().toString();
                    userInfo[2] = userName.getText().toString();
                    userInfo[3] = userEmail.getText().toString();
                    userTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id){
                            userInfo[4] = adapterView.getItemAtPosition(position).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView){

                        }
                    });

                    SignUpJson signIn = new SignUpJson();
                    signIn.execute(userInfo);       // id, email, name, password

                    finish();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public class SignUpJson extends AsyncTask<String, Void, String> {
        private int statusCode;
        public String doInBackground(String... params) {
            String id = params[0];
            String password = params[1];
            String name = params[2];
            String email = params[3];
            String userType = params[4];

            try {
                // 로그인 시 입력한 정보를 Json object로 만들어서
                JSONObject myJsonObject = new JSONObject();
                try {//id, password, name, email. user type
                    myJsonObject.put("username", id);
                    myJsonObject.put("input_password", password);
                    myJsonObject.put("name",name);
                    myJsonObject.put("email", email);
                    myJsonObject.put("user_type",userType);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // 서버 api에 전송을 시도한다
                URL obj = new URL("http://ec2-3-34-135-151.ap-northeast-2.compute.amazonaws.com/api/v1/users/signup/");
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
            Log.d("json","on post execute");

            if (s == null) {
                // 서버에서 널 값이 온경우. API가 이상하거나. 서버가 꺼져있는 경우
                Log.d("json","------------------------null from server");
            } else {
                try {
                    // 수신한 data s에 대해
                    JSONObject jsonObject = new JSONObject(s);

                    //아이디 중복시
                    if(statusCode == 201){
                        // 데이터들을 추출하여 변수에 저장한다.
                        // 아이디 중복시 300
                        Log.d("json","--------------------회원가입성공");
                        String idFromServer = jsonObject.get("username").toString();
                        Toast.makeText(getApplicationContext(),idFromServer + " 회원가입 성공",Toast.LENGTH_SHORT).show();
                    }
                    if(statusCode == 400){
                        Log.d("json","--------------------아이디 중복");
                        Toast.makeText(getApplicationContext(),"이미 사용 중인 아이디입니다.",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {

                }
            }
        }

    }
}
