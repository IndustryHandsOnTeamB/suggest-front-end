package com.example.handson;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MbtiActivity extends AppCompatActivity {
    Button eButton, iButton, nButton, sButton, tButton, fButton, jButton, pButton;
    boolean iBtnPressed = false, nBtnPressed = false, tBtnPressed = false, jBtnPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_set_mbti);

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
                Log.v("Tag","내 mbti: "+myMbti);
                //TO-DO : myMbti 서버에 저장
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
        btn1.setBackgroundColor(Color.YELLOW);
        btn2.setBackgroundColor(Color.LTGRAY);
    }

    String getMbtiFromButtons(){
        String myMbti = "";
        if(iBtnPressed) myMbti=myMbti.concat("i");
        else  myMbti=myMbti.concat("e");
        if(nBtnPressed) myMbti=myMbti.concat("n");
        else  myMbti=myMbti.concat("s");
        if(tBtnPressed) myMbti=myMbti.concat("t");
        else  myMbti=myMbti.concat("f");
        if(jBtnPressed) myMbti=myMbti.concat("j");
        else  myMbti=myMbti.concat("p");

        return myMbti;
    }
}
