package com.example.handson;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final boolean usableId = false;
        final String[] userTypeList = {"중학생","고등학생","대학생","기타"};

        final Spinner userTypeSpinner = (Spinner)findViewById(R.id.user_type_spinner);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, userTypeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSpinner.setAdapter(adapter);

        Button idCheckButton = (Button)findViewById(R.id.id_check_button);
        Button signUpButton = (Button)findViewById(R.id.join_button);
        Button cancelButton = (Button)findViewById(R.id.cancel_button);

        final EditText userName = (EditText)findViewById(R.id.user_name);
        final EditText userId = (EditText)findViewById(R.id.user_id);
        final EditText userPwd = (EditText)findViewById(R.id.user_pwd);
        final EditText userPwdCheck = (EditText)findViewById(R.id.user_pwd_check);



        idCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newId = userId.getText().toString();
                //같은 아이디 있는지 check
                if(usableId)
                    Toast.makeText(getApplicationContext(),"사용가능한 아이디입니다.",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(),"이미 사용중인 아이디입니다.",Toast.LENGTH_SHORT).show();

            }
        });

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
                    final String[] userInfo = new String[4];
                    userInfo[0] = userName.getText().toString();
                    userInfo[1] = userId.getText().toString();
                    userInfo[2] = userPwd.getText().toString();
                    userTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id){
                            userInfo[3] = adapterView.getItemAtPosition(position).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView){

                        }
                    });
                    //Intent intent = new Intent(getApplicationContext(), nextActivity.class);
                    //intent.putExtra("userInfo", userInfo);
                    //startActivity(intent);
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
}
