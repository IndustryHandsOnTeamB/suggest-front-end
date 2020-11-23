package com.example.handson;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

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
}