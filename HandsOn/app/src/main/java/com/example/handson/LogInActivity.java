package com.example.handson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LogInActivity extends AppCompatActivity {

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
                Intent intent = new Intent(getApplicationContext(), MenuSelect.class);
                intent.putExtra("userId", id);
                intent.putExtra("userPwd", pwd);
                finish();
                startActivity(intent);
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
}
